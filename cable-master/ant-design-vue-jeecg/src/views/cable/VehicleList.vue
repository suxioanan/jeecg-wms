<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="7">
            <a-form-item label="车牌号码">
              <a-input placeholder="请输入车牌号码" v-model="queryParam.license"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="车辆类型">
              <a-select v-model="queryParam.type" placeholder="请选择车辆类型">
                <template v-for="(types,index) in vehicleTypes">
                  <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="3" :sm="7">
            <a-form-item label="状态">
              <a-select v-model="queryParam.state" placeholder="请选择状态">
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">维修</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="10" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" style="margin-left: 8px" icon="plus">新增车辆</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('车辆表')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
          <j-ellipsis :value="text" :length="100"/>
        </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">修改</a>
        </span>

      </a-table>
    </div>

    <vehicle-modal ref="modalForm" @ok="modalFormOk"></vehicle-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VehicleModal from './modules/VehicleModal'
  import JInput from '@/components/jeecg/JInput'
  import {httpAction, getAction, putAction, deleteAction} from '@/api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: "VehicleList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      VehicleModal,
      JInput,
      JEllipsis
    },
    data () {
      return {
        description: '车辆表管理页面',
        //车辆类型
        vehicleTypes:{},
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title:'车牌号码',
            align:"center",
            dataIndex: 'license',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'车辆类型',
            align:"center",
            dataIndex: 'type_dictText',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'载重(吨)',
            align:"center",
            dataIndex: 'carryingCapacity',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'发动机号',
            align:"center",
            dataIndex: 'engineNumber',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state_dictText',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'text',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/cable/vehicle/list",
          delete: "/cable/vehicle/delete",
          deleteBatch: "/cable/vehicle/deleteBatch",
          exportXlsUrl: "/cable/vehicle/exportXls",
          importExcelUrl: "cable/vehicle/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
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
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      //删除车辆
      handleDel(val){
        deleteAction(this.url.delete,{id:val}).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.loadData()
          } else {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      //车辆类型加载
      vehicleType() {
        console.log("vehicleType")
        getAction("/sys/dictItem/selectVehicleType").then((res) => {
          if (res.success) {
            this.vehicleTypes = res.result;
          }
        })
      },
    },
    created () {
      this.vehicleType();
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>