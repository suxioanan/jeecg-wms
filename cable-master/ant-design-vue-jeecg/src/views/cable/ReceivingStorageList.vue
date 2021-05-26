<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('出库/完单表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <receivingStorage-modal ref="modalForm" @ok="modalFormOk"></receivingStorage-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReceivingStorageModal from './modules/ReceivingStorageModal'

  export default {
    name: "ReceivingStorageList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ReceivingStorageModal
    },
    data () {
      return {
        description: '出库/完单表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'项目编号',
            align:"center",
            dataIndex: 'projectNo'
          },
          {
            title:'处置类型',
            align:"center",
            dataIndex: 'type'
          },
          {
            title:'物料id',
            align:"center",
            dataIndex: 'materialId'
          },
          {
            title:'资产描述',
            align:"center",
            dataIndex: 'assetText'
          },
          {
            title:'出库数量',
            align:"center",
            dataIndex: 'receivingNum'
          },
          {
            title:'审核状态(0未审核 1已审)',
            align:"center",
            dataIndex: 'state'
          },
          {
            title:'情况',
            align:"center",
            dataIndex: 'sceneSituation'
          },
          {
            title:'现场异常照片路径(路径用逗号隔开)',
            align:"center",
            dataIndex: 'sceneAbnormalPhotos'
          },
          {
            title:'回单照片路径(路径用逗号隔开)',
            align:"center",
            dataIndex: 'receiptPhotos'
          },
          {
            title:'现场情况',
            align:"center",
            dataIndex: 'situationText'
          },
          {
            title:'出库日期',
            align:"center",
            dataIndex: 'receivingTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'情况说明',
            align:"center",
            dataIndex: 'sceneKn'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'backup1',
            align:"center",
            dataIndex: 'backup1'
          },
          {
            title:'backup2',
            align:"center",
            dataIndex: 'backup2'
          },
          {
            title:'backup3',
            align:"center",
            dataIndex: 'backup3'
          },
          {
            title:'backup4',
            align:"center",
            dataIndex: 'backup4'
          },
          {
            title:'backup5',
            align:"center",
            dataIndex: 'backup5'
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
          list: "/cable/receivingStorage/list",
          delete: "/cable/receivingStorage/delete",
          deleteBatch: "/cable/receivingStorage/deleteBatch",
          exportXlsUrl: "/cable/receivingStorage/exportXls",
          importExcelUrl: "cable/receivingStorage/importExcel",
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
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>