
<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.serial"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.materialName"></a-input>
            </a-form-item>
          </a-col>

          <!--<a-col :md="4" :sm="7">
            <a-form-item label="规格型号">
              <a-input placeholder="请输入规格型号" v-model="queryParam.ations"></a-input>
            </a-form-item>
          </a-col>-->

          <!--<a-col :md="4" :sm="7">
            <a-form-item label="计划类型">
              <a-select v-model="queryParam.pType" placeholder="请选择计划类型">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="备品">备品</a-select-option>
                <a-select-option value="新品">新品</a-select-option>
                <a-select-option value="抢修">抢修</a-select-option>
                <a-select-option value="临措">临措</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="资产编号">
              <a-input placeholder="请输入资产编号" v-model="queryParam.pAssetNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="交接单号">
              <a-input placeholder="请输入交接单号" v-model="queryParam.pItemSlip"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="仓库名称">
              <a-input placeholder="请输入仓库名称" v-model="queryParam.warehouseName"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">

            <a-col :md="6" :sm="7">
              <a-form-item label="项目编号/工程账号">
                <a-input placeholder="请输入项目编号/工程账号" v-model="queryParam.projectNo"></a-input>
              </a-form-item>
            </a-col>

          </template>-->

          <a-col :md="6" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--<a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>-->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"-->
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange">
        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="20"/>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看详情</a>
        </span>
      </a-table>
    </div>
    <inventory-modal ref="InventoryModal" @ok="modalFormOk"></inventory-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import InventoryModal from './modules/InventoryModal'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'InventoryList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      InventoryModal,
      JEllipsis
    },
    data() {
      return {
        description: '库存表管理页面',
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '物料编号',
            align: 'center',
            dataIndex: 'serial',
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'materialName',
          },
          {
            title: '库存数',
            align: 'center',
            dataIndex: 'inventoryQuantity',
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'dw_dictText',
          },
          {
            title: '重量',
            align: 'center',
            dataIndex: 'availableWeight',
            customRender: (text,record) => {
              if (record.availableWeight != '' && record.availableWeight != null)
                return record.availableWeight + '吨'
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            // fixed:"right",
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
          /*{
            title: '计划类型',
            align: 'center',
            dataIndex: 'ptype',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '资产编号',
            align: 'center',
            dataIndex: 'passetNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '交接单号',
            align: 'center',
            dataIndex: 'pitemSlip',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '仓库名称',
            align: 'center',
            dataIndex: 'warehouseName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目编码',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },*/
        ],
        url: {
          list: '/cable/warehouse/inventoryIocationList'
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
      handleEdit(record) {
        this.$refs.InventoryModal.outshwoz(record)
      }
    }
  }
</script>
<!--<template>-->
<!--  <a-card :bordered="false">-->
<!--    &lt;!&ndash; 查询区域 &ndash;&gt;-->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->

<!--          <a-col :md="5" :sm="7">-->
<!--            <a-form-item label="计划类型">-->
<!--&lt;!&ndash;              <a-input placeholder="请选择计划类型" v-model="queryParam.planType"></a-input>&ndash;&gt;-->
<!--              <a-select v-model="queryParam.planType" placeholder="请选择计划类型">-->
<!--                <a-select-option value="配变电">配变电</a-select-option>-->
<!--                <a-select-option value="其他">其他</a-select-option>-->
<!--                <a-select-option value="电缆">电缆</a-select-option>-->
<!--                <a-select-option value="线路">线路</a-select-option>-->
<!--                <a-select-option value="备品">备品</a-select-option>-->
<!--                <a-select-option value="新品">新品</a-select-option>-->
<!--                <a-select-option value="抢修">抢修</a-select-option>-->
<!--                <a-select-option value="临措">临措</a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="5" :sm="7">-->
<!--            <a-form-item label="项目编号/工程账号">-->
<!--              <a-input placeholder="请输入项目编号/工程账号" v-model="queryParam.projectNo"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="5" :sm="7">-->
<!--            <a-form-item label="仓库名称">-->
<!--              <a-input placeholder="请输入仓库名称" v-model="queryParam.warehouseName"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <template v-if="toggleSearchStatus">-->

<!--            <a-col :md="5" :sm="7">-->
<!--              <a-form-item label="物料名称">-->
<!--                <a-input placeholder="请输入物料名称" v-model="queryParam.materialName"></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--            <a-col :md="5" :sm="7">-->
<!--              <a-form-item label="物料编号">-->
<!--                <a-input placeholder="请输入物料编号" v-model="queryParam.serial"></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          </template>-->

<!--          <a-col :md="7" :sm="10">-->
<!--            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
<!--              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
<!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
<!--            </span>-->
<!--          </a-col>-->

<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->
<!--    &lt;!&ndash; 查询区域-END &ndash;&gt;-->
<!--    &lt;!&ndash; table区域-begin &ndash;&gt;-->
<!--    <div>-->
<!--      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
<!--        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{-->
<!--        selectedRowKeys.length }}</a>项-->
<!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
<!--      </div>-->

<!--      <a-table-->
<!--        ref="table"-->
<!--        size="middle"-->
<!--        bordered-->
<!--        :columns="columns"-->
<!--        :dataSource="dataSource"-->
<!--        :pagination="ipagination"-->
<!--        :loading="loading"-->
<!--        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"-->
<!--        class="j-table-force-nowrap"-->
<!--        @change="handleTableChange">-->
<!--        <span slot="factoryText" slot-scope="text">-->
<!--          <j-ellipsis :value="text" :length="20"/>-->
<!--        </span>-->
<!--        <span slot="action" slot-scope="text, record">-->
<!--          <a @click="handleEdit(record)">查看库位</a>-->
<!--        </span>-->
<!--      </a-table>-->
<!--    </div>-->
<!--    <inventory-modal ref="InventoryModal" @ok="modalFormOk"></inventory-modal>-->
<!--  </a-card>-->
<!--</template>-->

<!--<script>-->

<!--  import '@/assets/less/TableExpand.less'-->
<!--  import { mixinDevice } from '@/utils/mixin'-->
<!--  import { JeecgListMixin } from '@/mixins/JeecgListMixin'-->
<!--  import InventoryModal from './modules/InventoryModal'-->
<!--  import JEllipsis from '../../components/jeecg/JEllipsis'-->

<!--  export default {-->
<!--    name: 'InventoryList',-->
<!--    mixins: [JeecgListMixin, mixinDevice],-->
<!--    components: {-->
<!--      InventoryModal,-->
<!--      JEllipsis-->
<!--    },-->
<!--    data() {-->
<!--      return {-->
<!--        description: '库存表管理页面',-->
<!--        // 查询条件-->
<!--        queryParam: {},-->
<!--        // 表头-->
<!--        columns: [-->
<!--          {-->
<!--            title: '计划类型',-->
<!--            align: 'center',-->
<!--            dataIndex: 'planType',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '仓库名称',-->
<!--            align: 'center',-->
<!--            dataIndex: 'name',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '项目编码',-->
<!--            align: 'center',-->
<!--            dataIndex: 'projectNo',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '项目名称',-->
<!--            align: 'center',-->
<!--            dataIndex: 'projectName',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '物料编号',-->
<!--            align: 'center',-->
<!--            dataIndex: 'serial',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '物料名称',-->
<!--            align: 'center',-->
<!--            dataIndex: 'materialName',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '库存数',-->
<!--            align: 'center',-->
<!--            dataIndex: 'currentInventory',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '单位',-->
<!--            align: 'center',-->
<!--            dataIndex: 'unit_dictText',-->
<!--            scopedSlots: { customRender: 'factoryText' }-->
<!--          },-->
<!--          {-->
<!--            title: '操作',-->
<!--            dataIndex: 'action',-->
<!--            align: 'center',-->
<!--            // fixed:"right",-->
<!--            width: 147,-->
<!--            scopedSlots: { customRender: 'action' }-->
<!--          }-->
<!--        ],-->
<!--        url: {-->
<!--          list: '/cable/warehouse/inventoryIocationList'-->
<!--        },-->
<!--        dictOptions: {}-->
<!--      }-->
<!--    },-->
<!--    computed: {-->
<!--      importExcelUrl: function() {-->
<!--        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`-->
<!--      }-->
<!--    },-->
<!--    methods: {-->
<!--      handleEdit(record) {-->
<!--        this.$refs.InventoryModal.outshwoz(record)-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--</script>-->