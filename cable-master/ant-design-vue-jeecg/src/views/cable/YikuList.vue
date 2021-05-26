<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="7">
            <a-form-item label="仓库">
              <a-select v-model="queryParam.name1" placeholder="请选择仓库" @change="warehouses">
                <template v-for="(warehouse,index) in warehouseLists">
                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="7">
            <a-form-item label="库位">
              <a-select v-model="queryParam.name2" placeholder="请选择库位">
                <template v-for="(storageLocation,index) in storageLocations">
                  <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.wuliaoName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
<!--    <div class="table-operator">-->
<!--      &lt;!&ndash;      <a-button type="primary" icon="download" @click="handleExportXls('车辆表')">导出</a-button>&ndash;&gt;-->
<!--      &lt;!&ndash;      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">&ndash;&gt;-->
<!--      &lt;!&ndash;        <a-button type="primary" icon="import">导入</a-button>&ndash;&gt;-->
<!--      &lt;!&ndash;      </a-upload>&ndash;&gt;-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel">-->
<!--            <a-icon type="delete"/>-->
<!--            删除-->
<!--          </a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作-->
<!--          <a-icon type="down"/>-->
<!--        </a-button>-->
<!--      </a-dropdown>-->
<!--    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="20"/>
        </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDel(record.id)">
                  <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VehicleModal from './modules/VehicleModal'
  import JInput from '@/components/jeecg/JInput'
  import { httpAction, getAction, putAction, deleteAction } from '@/api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: 'YikuList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      VehicleModal,
      JInput,
      JEllipsis
    },
    data() {
      return {
        warehouseLists: [],
        storageLocations: [],
        description: '车辆表管理页面',
        //车辆类型
        vehicleTypes: {},
        // 查询条件
        queryParam: {
          name1: undefined,
          name2: undefined,
          wuliaoName: undefined
        },
        // 表头
        columns: [
          {
            title: '原位置',
            align: 'center',
            dataIndex: 'name1',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '移库后位置',
            align: 'center',
            dataIndex: 'name2',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '物料编码',
            align: 'center',
            dataIndex: 'wcode',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'wuliaoName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '数量',
            align: 'center',
            dataIndex: 'yksum',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'unit_dictText',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '记录时间',
            align: 'center',
            dataIndex: 'createTime',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            // fixed:"right",
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/cable/inventory/yikuList',
          delete: '/cable/inventory/yikuDel'
        },
        dictOptions: {}
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      storageLocationList(val) {
        this.storageLocations.length = 0
        getAction('/cable/storageLocation/list2', {
          warehouseId: val
        }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log('加载库位')
            console.log(this.storageLocations)
          }
        })
      },
      warehouses(val) {
        this.storageLocations.length = []
        this.queryParam.name2 = undefined
        this.storageLocationList(val)
      },
      //重新加载数据
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        var params = this.getQueryParams()//查询条件
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      //删除车辆
      handleDel(val) {
        deleteAction(this.url.delete, { id: val }).then((res) => {
          if (res.success) {
            this.loadData()
          } else {
            window.confirm('该车辆被派单过，不能删除')
          }
        })
      },
      getWarehouseList() {
        getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
            console.log(this.warehouseLists)
          }
        })
      },

    },
    created() {
      this.getWarehouseList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>