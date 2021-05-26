<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="类型">
              <a-select v-model="queryParam.planType" placeholder="请选择所属类型">
                <a-select-option v-for="(item,index) in plan3TypeList" :value="item.itemText">{{item.itemText}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目编号">
              <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.engName"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="true">
            <!--          <template v-if="toggleSearchStatus">-->
            <a-col :md="4" :sm="7">
              <a-form-item label="采购订单号">
                <a-input placeholder="请输入采购订单号" v-model="queryParam.proTheorderNo"></a-input>
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
              <a-button type="primary" icon="import" @click="showImportModal" style="margin-left: 8px">导入</a-button>
              <a-modal
                v-model="visible"
                :width=400
                on-ok="handleCancel" style="padding-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form
                  :form="form"
                  :label-col="{ span: 5 }"
                  :wrapper-col="{ span: 12 }" style="padding-top: 20px">

                  <a-form-item label="导入类型" style="width: 400px;margin-left: 20px">
                    <a-select v-model="planType" placeholder="选择导入类型">
                       <a-select-option v-for="(item,index) in plan3TypeList" :value="item.itemText">{{item.itemText}}
                      </a-select-option>
                    </a-select>
                  </a-form-item>

                  <a-form-item label="选择文件" style="width: 400px;margin-left: 20px">
                    <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                              :action="importExcelUrl" @change="handleImportExcel">
                      <a-button>选择文件</a-button>
                    </a-upload>
                  </a-form-item>

                </a-form>
                <!-- 自定义内容-END -->

                <!-- 自定义页脚-begin -->
                <template slot="footer">
                  <a-button @click="handleCancel">关闭</a-button>
                </template>
                <!-- 自定义页脚-END -->

              </a-modal>
              <a-button style="margin-left: 8px" type="primary" icon="download"
                        @click="showExportPlan3Modal">导出</a-button>
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
                v-model="plan3ExportModal_visible"
                :width=700 style="margin-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form
                  :form="plan3Exportform"
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
                  <a-button type="primary" @click="handleExportXls('新品临措')">导出反馈excel</a-button>
                  <a-button @click="handleCancelExportPlan3Modal">关闭</a-button>
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

    <!--    操作按钮区域-->
    <!-- <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('计划表3')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
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
          <j-ellipsis :value="text" :length="12"/>
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
          <a @click="plan3Status(record)">计划状态</a>
        </span>

      </a-table>
    </div>

    <plan3-modal ref="modalForm" @ok="modasFormOk"></plan3-modal>
    <planAccomplish-modal ref="planAccomplishModal" @ok="modasFormOk"></planAccomplish-modal>
    <planSendOrders-modal ref="planSendOrdersModal" @ok="modasFormOk"></planSendOrders-modal>
    <plan-send-orders-the-same-day-modal ref="planSendOrdersTheSameDayModal" @ok="modasFormOk"></plan-send-orders-the-same-day-modal>
    <plan-complete-state-modal ref="planCompleteStateModal" @ok="modasFormOk"></plan-complete-state-modal>
    <merge-plan ref="MergePlan" @ok="modasFormOk"></merge-plan>
    <merge-plan-model-plan1 ref="MergePlanModelPlan1" @ok="modasFormOk"></merge-plan-model-plan1>
    <complete-plan3-model ref="CompletePlan3Model" @ok="modasFormOk"></complete-plan3-model>
    <plan-send-orders-j-l-modal ref="planSendOrdersJLModal" @ok="modasFormOk"></plan-send-orders-j-l-modal>
    <!-- 派单记录 modal -->
    <plan-send-orders-wd-modal ref="planSendOrdersWdModal" @ok="modasFormOk"></plan-send-orders-wd-modal>
    <!-- 完单记录 modal -->
  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Plan3Modal from './modules/Plan3Modal'
  import JInput from '@/components/jeecg/JInput'
  import {httpAction, getAction} from '@/api/manage'
  import PlanSendOrdersModal from './modules/PlanSendOrdersModal'
  import PlanAccomplishModal from './modules/PlanAccomplishModal'
  import PlanSendOrdersTheSameDayModal from './modules/PlanSendOrdersTheSameDayModal'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'
  import {downFile} from '../../api/manage'
  import PlanCompleteStateModal from './modules/PlanCompleteStateModal'
  import MergePlan from "./modules/MergePlanModel";
  import MergePlanModelPlan1 from "./modules/MergePlanModelPlan1";
  import CompletePlan3Model from "./modules/CompletePlan3Model";
  import PlanSendOrdersJLModal from "./modules/PlanSendOrdersJLModal";
  import PlanSendOrdersWdModal from './modules/PlanSendOrdersWdModal';

  export default {
    name: 'Plan3List',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JDate,
      PlanSendOrdersWdModal,
      PlanSendOrdersJLModal,
      MergePlanModelPlan1,
      MergePlan,
      Plan3Modal,
      JInput,
      PlanSendOrdersModal,
      PlanAccomplishModal,
      JEllipsis,
      PlanSendOrdersTheSameDayModal,
      PlanCompleteStateModal,
      CompletePlan3Model
    },
    data() {
      return {
        description: '计划表3管理页面',
        width: 800,
        loading: false,
        visible: false,
        plan3ExportModal_visible: false,
        plan3ExportModal_width: 800,
        plan3Exportform: this.$form.createForm(this),
        confirmLoading: false,
        endOpen: false,
        form: this.$form.createForm(this),
        // 导入excel类型
        planType: '',
        //保存查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '工程名称',
            align: 'center',
            dataIndex: 'engName',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '采购订单号',
            align: 'center',
            dataIndex: 'proTheorderNo',
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'materialDescribe',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '需求数量',
            align: 'center',
            dataIndex: 'num',
          },
          /*{
            title: '单位',
            align: 'center',
            dataIndex: 'measuringUnit_dictText',
            scopedSlots: { customRender: 'factoryText' }
          },*/
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
        ],
        url: {
          list: '/cable/plan3/list',
          delete: '/cable/plan3/delete',
          deleteBatch: '/cable/plan3/deleteBatch',
          exportXlsUrl: '/cable/plan3/exportXls',
          importExcelUrl: 'cable/plan3/importExcel'
        },
        dictOptions: {},
        completeStates: {},
        plan3TypeList: {},
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
    computed: {},
    methods: {
      /**
       * 计划3合并完单
       */
      completePlan() {
        var ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并完单项目!')
        //TODO 打开合并完单页面
        this.$refs.CompletePlan3Model.completePlanModelShow(ids)
        this.$refs.CompletePlan3Model.title = '合并完单'
      },
      /**
       * 计划3完成状态
       */
      plan3Status(record) {
        console.log('计划3完成状态[0未完成 1已完成]', record)
        this.$refs.planCompleteStateModal.show(record, 3)
      },
      modasFormOk() {
        this.loadData()
      },
      loadData(arg) {
        if (!this.url.list) {
          //this.$message.error("请设置url.list属性!")
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
      /**
       * 派单记录
       */
      assignsJL() {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择查看派单记录的计划!')
        console.log('派单记录3', ids)
        this.$refs.planSendOrdersJLModal.dakpd(ids.toString(), 3)
        this.$refs.planSendOrdersJLModal.title = '派单记录'
      },
      /**
       * 完单记录
       */
      assignsWD(val) {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择派单项目!')
        console.log('完单记录3', ids)
        this.$refs.planSendOrdersWdModal.dakpd(ids.toString(), 3)
        this.$refs.planSendOrdersWdModal.title = '完单记录'
      },
      // 导出反馈excel
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
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], {type: 'application/vnd.ms-excel'}), fileName + '.xls')
            this.plan3ExportModal_visible = false
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
            this.plan3ExportModal_visible = false
          }
        })
      },
      showExportPlan3Modal() {
        this.plan3ExportModal_visible = true
      },
      handleCancelExportPlan3Modal() {
        this.plan3ExportModal_visible = false
      },
      /* 导入前操作 */
      importExcelUrl: function () {
        if (this.planType !== '') {
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}/` + this.planType
        } else {
          this.$message.warning('请选择导入类型')
          this.visible = false
          return false
        }
      },
      /* 导入后操作 */
      handleImportExcel(info) {
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList)
        }
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            // this.$message.success(`${info.file.name} 文件上传成功`);
            if (info.file.response.code === 201) {
              let {message, result: {msg, fileUrl, fileName}} = info.file.response
              let href = window._CONFIG['domianURL'] + fileUrl
              this.$warning({
                  title: message,
                  content: (
                    < div >
                    < span > {msg} < /span><br/ >
                    < span > 具体详情请 < a href = {href} target = '_blank' download = {fileName} > 点击下载 < /a> </span >
                < /div>
              )
            })
            } else {
              this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
              this.visible = false
              this.loadData()
            }
          } else {
            this.$message.error(`${info.file.name} ${info.file.response.message}.`)
          }
        } else if (info.file.status === 'error') {
          //this.$message.error(`文件上传失败: ${info.file.msg} `);
          this.visible = false
          this.loadData()
        }
      },
      showImportModal() {
        this.visible = true
      },
      handleCancel() {
        this.visible = false
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
       * 合并派单
       */
      mergePlan() {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并派单项目!')

        console.log("点击了合并派单", ids)
        //TODO 打开合并派单页面
        this.$refs.MergePlanModelPlan1.mergePlanModelShow(ids, 3)
        this.$refs.MergePlanModelPlan1.title = '合并派单'
        // this.$refs.MergePlan.mergePlanModelShow(ids,3)
        // this.$refs.MergePlan.title = '合并派单'
      },
      assigns(val) {
        console.log('派单')
        this.$refs.MergePlanModelPlan1.mergePlanModelShow(val.id, 3)
        // this.$refs.planSendOrdersModal.dakpd(val, 3)
        this.$refs.planSendOrdersModal.title = ''
      },
      //完单操作
      accomplish(val) {
        console.log('完单')
        this.$refs.CompletePlan3Model.completePlanModelShow(val.id)
        this.$refs.CompletePlan3Model.title = '合并完单'
        // this.$refs.planAccomplishModal.dakwd(val, 3)
        // this.$refs.planAccomplishModal.title = ''
      },
      completeStateList() {
        getAction('/sys/dictItem/selectCompleteState').then((res) => {
          if (res.success) {
            this.completeStates = res.result
          }
        })
      },
      /**
       * 查询所有计划3的字典信息
       */
      getPlan3TypeList() {
        getAction('/sys/dictItem/getSysDictItemCommon', {commonText: 'plan3_type'}).then((res) => {
          if (res.success) {
            this.plan3TypeList = res.result
          }
        })
      }
    },
    created() {
      this.completeStateList()
      this.getPlan3TypeList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>