<template>
  <a-card :bordered="false">
    <!-- 左侧面板 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="12">
          <a-col :md="4" :sm="8">
            <a-form-item label="部门名称" :labelCol="{span: 6}" :wrapperCol="{span: 14, offset: 1}">
              <a-input placeholder="请输入部门名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="7" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" icon="plus" style="margin-left: 8px">添加部门</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <a-table
        ref="table"
        bordered
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="30"/>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">
            <a-icon type="edit"/>
            编辑
          </a>
          <a-divider type="vertical"/>
          <a-popconfirm title="本次删除会清除员工所在部门" @confirm="() =>handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>
    <dict-modal ref="modalForm" @ok="modalFormOk"></dict-modal>  <!-- 字典类型 -->
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DictModal from './modules/BumenModal'
  import { getAction } from '@/api/manage'
  import { UI_CACHE_DB_DICT_DATA } from '@/store/mutation-types'
  import Vue from 'vue'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: 'Bumen',
    mixins: [JeecgListMixin],
    components: { DictModal, JEllipsis },
    data() {
      return {
        description: '',
        visible: false,
        // 查询条件
        queryParam: {
          dictCode: '',
          dictName: ''
        },
        // 表头
        columns: [
          {
            title: '部门名称',
            align: 'center',
            dataIndex: 'itemText',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '描述',
            align: 'center',
            dataIndex: 'description',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            width: 140,
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        dict: '',
        labelCol: {
          xs: { span: 8 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 16 },
          sm: { span: 19 }
        },
        url: {
          list: '/cable/testdata/bumenPage',
          delete: '/cable/testdata/delete'
        }
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      getQueryParams() {
        var param = Object.assign({}, this.queryParam, this.isorter)
        param.field = this.getQueryField()
        param.pageNo = this.ipagination.current
        param.pageSize = this.ipagination.pageSize
        if (this.superQueryParams) {
          param['superQueryParams'] = encodeURI(this.superQueryParams)
          param['superQueryMatchType'] = this.superQueryMatchType
        }
        return filterObj(param)
      },
      //取消选择
      cancelDict() {
        this.dict = ''
        this.visible = false
        this.loadData()
      },
      //编辑字典数据
      editDictItem(record) {
        this.$refs.dictItemList.edit(record)
      },
      // 重置字典类型搜索框的内容
      searchReset() {
        var that = this
        that.queryParam = {}
        that.loadData(this.ipagination.current)
      },
      openDeleteList() {
        this.$refs.dictDeleteList.show()
      },
      refleshCache() {
        getAction(this.url.refleshCache).then((res) => {
          if (res.success) {
            //重新加载缓存
            getAction(this.url.queryAllDictItems).then((res) => {
              if (res.success) {
                Vue.ls.remove(UI_CACHE_DB_DICT_DATA)
                Vue.ls.set(UI_CACHE_DB_DICT_DATA, res.result, 7 * 24 * 60 * 60 * 1000)
              }
            })
            this.$message.success('刷新缓存完成！')
          }
        }).catch(e => {
          this.$message.warn('刷新缓存失败！')
          console.log('刷新失败', e)
        })
      }
    },
    watch: {
      openKeys(val) {
        console.log('openKeys', val)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>