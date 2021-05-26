<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="资产编号">
              <a-input placeholder="请输入资产编号" v-model="queryParam.assetNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目编号">
              <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入站点名" v-model="queryParam.site"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="true">
            <!--          <template v-if="toggleSearchStatus">-->

            <a-col :md="4" :sm="7">
              <a-form-item label="设备名">
                <a-input placeholder="请输入设备名" v-model="queryParam.equipmentName"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="4" :sm="7">
              <a-form-item label="完成状态">
                <a-select v-model="queryParam.completeState" placeholder="请选择完成状态">
                  <template v-for="(types,index) in completeStates">
                    <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
                  </template>
                </a-select>
              </a-form-item>
            </a-col>
          </template>

          <a-col :md="7" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary"
                        style="margin-left: 8px;background-color: #00DB00;border: #00DB00" icon="plus">新建</a-button>
              <a-upload name="file" style="margin-left: 8px" :showUploadList="false" :multiple="false"
                        :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
              </a-upload>
              <a-button style="margin-left: 8px" type="primary" icon="download"
                        @click="showExportPlan2Modal">导出</a-button>
              <a-button @click="TheSameDay" icon="search" type="primary"
                        style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">今日派单</a-button>
                <a-button @click="mergePlan" icon="search" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">合并派单</a-button>
               <a-button @click="completePlan" icon="check-circle" type="primary"
                         style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">合并完单</a-button>
               <a-button @click="assignsJL" icon="check-circle" type="primary"
                         style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">派单记录</a-button>
                <a-button @click="assignsWD" icon="check-circle" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">完单记录</a-button>
              <a-modal
                v-model="plan2ExportModal_visible"
                :width=700 style="margin-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form
                  :form="plan2Exportform"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol">
                   <a-form-item label="起止日期">
                    <j-date v-model="queryParam.beginTime" placeholder="开始日期" showTime="true"
                            dateFormat="YYYY-MM-DD HH:mm:ss"></j-date>
                    <span>——</span>
                    <j-date v-model="queryParam.endTime" placeholder="结束日期" showTime="true"
                            dateFormat="YYYY-MM-DD HH:mm:ss"></j-date>
                  </a-form-item>
                  <a-form-item label="导出反馈说明" style="margin-top: 20px">
                    <a-input v-model="queryParam.explain" placeholder="请输入反馈说明" style="width: 370px"></a-input>
                  </a-form-item>
                </a-form>
                <!-- 自定义内容-END -->
                <!-- 自定义页脚-begin -->
                <template slot="footer">
                  <a-button type="primary" @click="handleExportXls('备品计划')">导出反馈excel</a-button>
                  <a-button @click="handleCancelExportPlan2Modal">关闭</a-button>
                </template>
                <!-- 自定义页脚-END -->
              </a-modal>
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

    <!-- 操作按钮区域 -->
    <!--<div class="table-operator">
      &lt;!&ndash;      <a-button type="primary" icon="download" @click="handleExportXls('计划表2')">导出</a-button>&ndash;&gt;
      &lt;!&ndash;      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">&ndash;&gt;
      &lt;!&ndash;        <a-button type="primary" icon="import">导入</a-button>&ndash;&gt;
      &lt;!&ndash;      </a-upload>&ndash;&gt;
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        rowKey="id"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="15"/>
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
          <a @click="handleEdit(record)">详情</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical"/>
          <!--<a @click="assigns(record)">派单</a>
          <a-divider type="vertical"/>
          <a @click="accomplish(record)">完单</a>
          <a-divider type="vertical"/>-->
          <a @click="plan2Status(record)">计划状态</a>
        </span>

      </a-table>
    </div>

    <plan2-modal ref="modalForm" @ok="modasFormOk"></plan2-modal>
    <planAccomplish-modal ref="planAccomplishModal" @ok="modasFormOk"></planAccomplish-modal>
    <planSendOrders-modal ref="planSendOrdersModal" @ok="modasFormOk"></planSendOrders-modal>
    <plan-send-orders-the-same-day-modal ref="planSendOrdersTheSameDayModal" @ok="modasFormOk"></plan-send-orders-the-same-day-modal>
    <plan-complete-state-modal ref="planCompleteStateModal" @ok="modasFormOk"></plan-complete-state-modal>
    <merge-plan ref="MergePlan" @ok="modasFormOk"></merge-plan>
    <merge-plan-model-plan1 ref="MergePlanModelPlan1" @ok="modasFormOk"></merge-plan-model-plan1>
    <complete-plan2-model ref="CompletePlan2Model" @ok="modasFormOk"></complete-plan2-model>
    <plan-send-orders-j-l-modal ref="planSendOrdersJLModal" @ok="modasFormOk"></plan-send-orders-j-l-modal>
    <!-- 派单记录 modal -->
    <plan-send-orders-wd-modal ref="planSendOrdersWdModal" @ok="modasFormOk"></plan-send-orders-wd-modal>
    <!-- 完单记录 modal -->
  </a-card>
</template>

<script>
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Plan2Modal from './modules/Plan2Modal'
  import PlanAccomplishModal from './modules/PlanAccomplishModal'
  import PlanSendOrdersModal from './modules/PlanSendOrdersModal'
  import PlanSendOrdersTheSameDayModal from './modules/PlanSendOrdersTheSameDayModal'
  import JInput from '@/components/jeecg/JInput'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction} from '@/api/manage'
  import {downFile} from '../../api/manage'
  import PlanCompleteStateModal from './modules/PlanCompleteStateModal'
  import MergePlan from "./modules/MergePlanModel";
  import MergePlanModelPlan1 from "./modules/MergePlanModelPlan1";
  import CompletePlan2Model from "./modules/CompletePlan2Model";
  import PlanSendOrdersJLModal from "./modules/PlanSendOrdersJLModal";
  import PlanSendOrdersWdModal from './modules/PlanSendOrdersWdModal';

  export default {
    name: 'Plan2List',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JDate,
      PlanSendOrdersWdModal,
      PlanSendOrdersJLModal,
      MergePlanModelPlan1,
      MergePlan,
      Plan2Modal,
      JInput,
      PlanAccomplishModal,
      PlanSendOrdersModal,
      JEllipsis,
      PlanSendOrdersTheSameDayModal,
      PlanCompleteStateModal,
      CompletePlan2Model
    },
    data() {
      return {
        description: '计划表2管理页面',
        form: this.$form.createForm(this),
        width: 800,
        loading: false,
        visible: false,
        plan2ExportModal_visible: false,
        plan2ExportModal_width: 800,
        plan2Exportform: this.$form.createForm(this),
        confirmLoading: false,
        endOpen: false,
        // 导入excel类型
        planType: '',
        //查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '退役资产编号',
            align: 'center',
            dataIndex: 'retiredAssetNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'site',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'backup2',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '型号',
            align: 'center',
            dataIndex: 'model',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '容量',
            align: 'center',
            dataIndex: 'capacity',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '入库状态',
            align: 'center',
            dataIndex: '',
            customRender: (text, record) => {
              if (record.alreadyDeliverStorage === null || record.alreadyDeliverStorage === undefined || record.alreadyDeliverStorage === 0) return '未入库'
              else return '已入库'
            }
          },
          {
            title: '入库数量',
            align: 'center',
            dataIndex: 'alreadyDeliverStorage',
            customRender: (value, row, index) => {
              if (value === null || value === undefined) return 0
              else return value
            }
          },
          {
            title: '出库状态',
            align: 'center',
            dataIndex: '',
            customRender: (text, record) => {
              if (record.alreadyReceivingStorage === null || record.alreadyReceivingStorage === undefined || record.alreadyReceivingStorage === 0) return '未出库'
              else return '已出库'
            }
          },
          {
            title: '出库数量',
            align: 'center',
            dataIndex: 'alreadyReceivingStorage',
            customRender: (value, row, index) => {
              if (value === null || value === undefined) return 0
              else return value
            }
          },
          {
            title: '派单状态',
            align: 'center',
            dataIndex: 'sendOrdersState',
            customRender: (value, row, index) => {
              if (value === 0) return '未派单'
              else return '已派单'
            }
          },
          {
            title: '计划状态',
            align: 'center',
            dataIndex: 'completeState_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            fixed: "right",
            width: 147,
            scopedSlots: {customRender: 'action'}
          }
          /*{
            title: '设备号',
            align: 'center',
            dataIndex: 'equipmentNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '退役时间',
            align: 'center',
            dataIndex: 'retiredDate',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '地址',
            align: 'center',
            dataIndex: 'address',
            scopedSlots: {customRender: 'factoryText'}
          },*/
        ],
        url: {
          list: '/cable/plan2/list',
          delete: '/cable/plan2/delete',
          deleteBatch: '/cable/plan2/deleteBatch',
          exportXlsUrl: '/cable/plan2/exportXls',
          importExcelUrl: 'cable/plan2/importExcel'
        },
        dictOptions: {},
        completeStates: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      /**
       * 计划2合并完单事件
       */
      completePlan() {
        var ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并完单项目!')
        //TODO 打开合并完单页面
        this.$refs.CompletePlan2Model.completePlanModelShow(ids)
        this.$refs.CompletePlan2Model.title = '合并完单'
      },
      /**
       * 计划2完成状态
       */
      plan2Status(record) {
        console.log('计划2完成状态[0未完成 1已完成]', record)
        this.$refs.planCompleteStateModal.show(record, 2)
      },
      modasFormOk() {
        this.loadData()
      },
      loadData(arg) {
        //清空勾选
        this.selectedRowKeys = []
        if(!this.url.list){
          //this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      /* 导出 */
      handleExportXls(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        let param = {...this.queryParam}
        if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
          param['selections'] = this.selectedRowKeys.join(',')
        }
        console.log('导出参数', param)
        downFile(this.url.exportXlsUrl, param).then((data) => {
          if (!data) {
            this.$message.warning('文件下载失败')
            this.plan2ExportModal_visible = false
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], {type: 'application/vnd.ms-excel'}), fileName + '.xls')
          } else {
            let url = window.URL.createObjectURL(new Blob([data], {type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName + '.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) //下载完成移除元素
            window.URL.revokeObjectURL(url) //释放掉blob对象
            this.plan2ExportModal_visible = false
          }
        })
      },
      /* 弹出导出反馈说明框 */
      showExportPlan2Modal() {
        this.plan2ExportModal_visible = true
      },
      /* 隐藏导出反馈说明框 */
      handleCancelExportPlan2Modal() {
        this.plan2ExportModal_visible = false
      },
      /* 弹出导入excel选择框 */
      showImportModal() {
        this.visible = true
      },
      /* 隐藏导入excel选择框 */
      handleCancel() {
        this.visible = false
      },
      /* 加载字典数据完成状态 */
      completeStateList() {
        getAction('/sys/dictItem/selectCompleteState').then((res) => {
          if (res.success) {
            this.completeStates = res.result
          }
        })
      },
      /**
       * 今日派单
       */
      TheSameDay() {
        console.log("今日派单")
        this.$refs.planSendOrdersTheSameDayModal.theSameDays()
        this.$refs.planSendOrdersTheSameDayModal.title = '今日派单'
      },
      /**
       * 派单记录
       */
      assignsJL() {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择查看派单记录的计划!')
        console.log('派单记录2', ids)
        this.$refs.planSendOrdersJLModal.dakpd(ids.toString(), 2)
        this.$refs.planSendOrdersJLModal.title = '派单记录'
      },
      /**
       * 完单记录
       */
      assignsWD(val) {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择派单项目!')
        console.log('完单记录2', ids)
        this.$refs.planSendOrdersWdModal.dakpd(ids.toString(), 2)
        this.$refs.planSendOrdersWdModal.title = '完单记录'
      },
      /**
       * 合并派单
       */
      mergePlan() {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并派单项目!')
        console.log("点击了合并派单", ids)
        //TODO 打开合并派单页面
        this.$refs.MergePlanModelPlan1.mergePlanModelShow(ids, 2)
        this.$refs.MergePlanModelPlan1.title = '合并派单'
        // this.$refs.MergePlan.mergePlanModelShow(ids,2)
        // this.$refs.MergePlan.title = '合并派单'
      },
      /* 派单操作 */
      assigns(val) {
        console.log('计划2派单', val)
        console.log("dakpd《《《《《《《《♥》》》》》》》》》:1")
        this.$refs.MergePlanModelPlan1.mergePlanModelShow(val.id, 2)
        // this.$refs.planSendOrdersModal.dakpd(val, 2)
        this.$refs.planSendOrdersModal.title = ''
      },
      /* 完单操作 */
      accomplish(val) {
        console.log('完单')
        this.$refs.CompletePlan2Model.completePlanModelShow(val.id)
        this.$refs.CompletePlan2Model.title = '完单'
        // this.$refs.planAccomplishModal.dakwd(val, 2)
        // this.$refs.planAccomplishModal.title = ''
      }
    },
    created() {
      this.completeStateList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>