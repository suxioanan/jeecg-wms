<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="close"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 30px">
    <a-spin :spinning="confirmLoading" style="margin-top: 10px">
      <a-form
        :form="form"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }">


        <div style="">
          <div style="margin-top: 10px;">
            <div style="display: inline-block;width: 100%;">
              <a-table
                ref="table"
                size="middle"
                bordered
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                @change="handleTableChange"
                class="j-table-force-nowrap">

                <span slot="factorynum" slot-scope="text, record">
                  {{record.accomplishNum + record.accomplishnumunit_dictText}}
                </span>

                <span slot="factoryWeight" slot-scope="text, record">
                  {{record.accomplishWeight + record.accomplishweightunit_dictText}}
                </span>

                <span slot="factoryText" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="realname" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="action" v-has="'orderJL:action'" slot-scope="text, record">
                  <a-popconfirm v-has="'orderWD:del'" title="确定删除吗?" @confirm="() => sdhandleDelete(record)">
                    <a>删除</a>
                  </a-popconfirm>
                  <a-divider type="vertical"/>
                  <a v-has="'orderWD:edit'" @click="handleEdits(record)">编辑</a>
                </span>

                <span slot="receiptPhotos" slot-scope="text,record">
                <template v-if="record.receiptPhotos!=undefined&&record.receiptPhotos!=null&&record.receiptPhotos!=''">
                  <a @click="() => getUrlNewView(record.receiptPhotos)">点我查看</a>
                </template>
                <template v-else>
                  <!--         暂无-->
                </template>
              </span>

                <span slot="scenePhotos1" slot-scope="text,record">
                   <template v-if="record.scenePhotos1!=undefined&&record.scenePhotos1!=null&&record.scenePhotos1!=''">
                     <a @click="() => getUrlNewView(record.scenePhotos1)">点我查看</a>
                   </template>
                <template v-else>
<!--                  暂无-->
                </template>
                </span>
                <span slot="scenePhotos2" slot-scope="text,record">
                <template v-if="record.scenePhotos2!=undefined&&record.scenePhotos2!=null&&record.scenePhotos2!=''">
                  <a @click="() => getUrlNewView(record.scenePhotos2)">点我查看</a>
                 </template>
                <template v-else>
<!--         暂无-->
                </template>
                </span>
              </a-table>
            </div>
          </div>

        </div>

      </a-form>
    </a-spin>

    <plan-accomplish-modal ref="planAccomplishModal"></plan-accomplish-modal>
    <plan-the-same-day-de-modal ref="planTheSameDayDeModal" @ok=""></plan-the-same-day-de-modal>

    <wandan-plan-xg-model ref="WandanPlanXgModel" @ok="modalFormOk"></wandan-plan-xg-model><!-- 计划 1 完单修改 -->
    <wandan-plan-xg2-model ref="WandanPlanXg2Model" @ok="modalFormOk"></wandan-plan-xg2-model><!-- 计划 2 完单修改 -->
    <wandan-plan-xg3-model ref="WandanPlanXg3Model" @ok="modalFormOk"></wandan-plan-xg3-model><!-- 计划 3 完单修改 -->
    <wandan-plan-xg4-model ref="WandanPlanXg4Model" @ok="modalFormOk"></wandan-plan-xg4-model><!-- 计划 4 完单修改 -->

    <!-- 自定义页脚 -->
    <template slot="footer">
      <a-button key="back" @click="handleCancel">
        关闭
      </a-button>
    </template>
  </a-modal>
</template>


<script>
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction, deleteAction} from '@/api/manage'
  import PlanAccomplishModal from './PlanAccomplishModal'
  import '@/assets/less/TableExpand.less'
  import PlanTheSameDayDeModal from './PlanTheSameDayDeModal'
  import { colAuthFilter } from "@/utils/authFilter"
  import WandanPlanXgModel from "./WandanPlanXgModel";
  import WandanPlanXg2Model from "./WandanPlanXg2Model";
  import WandanPlanXg3Model from "./WandanPlanXg3Model";
  import WandanPlanXg4Model from "./WandanPlanXg4Model";

  export default {
    name: 'PlanSendOrdersWdModal',
    components: {
      WandanPlanXg2Model,
      WandanPlanXg3Model,
      WandanPlanXg4Model,
      WandanPlanXgModel,
      JDate,
      JEllipsis,
      PlanAccomplishModal,
      PlanTheSameDayDeModal
    },
    data() {
      return {
        ipagination: {
          pageNo: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '6', '7', '8', '9', '10'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        warehouseLists: [],
        sendOrdersTypes: [],
        storageLocations: [],
        dataSource: [],
        columns: [
          {
            title: '派单类型',
            align: 'center',
            dataIndex: 'planType',
            width: 80
          },
          {
            title: '自家仓库',
            align: 'center',
            dataIndex: 'wname',
            width: 90
          },
          {
            title: '自家库位',
            align: 'center',
            dataIndex: 'storagename'
          },
          {
            title: '终点仓库',
            align: 'center',
            dataIndex: 'mubiaock',
            width: 90
          },
          {
            title: '任务日期',
            align: 'center',
            dataIndex: 'taskTime'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '物料描述',
            align: 'center',
            dataIndex: 'rawMaterialText',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '完单数量',
            align: 'center',
            dataIndex: 'accomplishNum',
            scopedSlots: {customRender: 'factorynum'},
            // customRender: (text, record) => {
            //   if (record.accomplishNum !== undefined && record.accomplishnumunit_dictText !== undefined)
            //     return record.accomplishNum + record.accomplishnumunit_dictText
            //   if (record.accomplishNum !== undefined && record.accomplishnumunit_dictText === undefined)
            //     return record.accomplishNum
            //   if (record.accomplishNum === undefined && record.accomplishnumunit_dictText !== undefined)
            //     return ''
            // }
          },
          // {
          //   title: '数量单位',
          //   align: 'center',
          //   dataIndex: 'accomplishnumunit_dictText'
          // },
          {
            title: '重量',
            align: 'center',
            dataIndex: 'accomplishWeight',
            scopedSlots: {customRender: 'factoryWeight'}
          },
          // {
          //   title: '重量单位',
          //   align: 'center',
          //   dataIndex: 'accomplishweightunit_dictText'
          // },
          {
            title: '回单图片',
            align: 'center',
            dataIndex: 'receiptPhotos',
            scopedSlots: {customRender: 'receiptPhotos'}
          },
          {
            title: '是否异常',
            align: 'center',
            dataIndex: 'sceneSituation',
            customRender: (value, row, index) => {
              var y = ''
              if (value == '0') {
                y = '正常'
              } else {
                y = '异常'
              }
              return y
            }
          },
          {
            title: '异常原因',
            align: 'center',
            dataIndex: 'anomalousCause'
          },
          {
            title: '异常照片1',
            align: 'center',
            dataIndex: 'scenePhotos1',
            scopedSlots: {customRender: 'scenePhotos1'},
          },
          {
            title: '异常照片2',
            align: 'center',
            dataIndex: 'scenePhotos2',
            scopedSlots: {customRender: 'scenePhotos2'},
          },
          {
            title: '说明',
            align: 'center',
            dataIndex: 'annotation',
            scopedSlots: {customRender: 'factoryText'},
          },
          {
            title: '操作',
            dataIndex: 'action',
            width: 50,
            align: 'center',
            scopedSlots: {customRender: 'action'}
          }
        ],
        form: this.$form.createForm(this),
        title: '操作',
        width: 1500,
        visible: false,
        model: {},
        confirmLoading: false,
        url: {
          add: '/cable/sendOrders/add',
          deleteWangda: '/cable/sendOrders/delete',
          edit: '/cable/sendOrders/edit',
          wddelete: '/cable/sendOrders/wddelete'
        },
        //查询参数
        ids: '',
        paln: '',
        data: {},
        vehicles: {},
        users: {},
        serials: '',
        storageLocations2: [],
        vadjsoifjweoi: {},
        // 派单类型
        sendOrderType: false
      }
    },
    created() {
      this.cloer()
      //权限
      this.disableMixinCreated=true;
      this.columns = colAuthFilter(this.columns,'orderJL:');
      this.initDictConfig();
    },
    methods: {
      handleEdits (record) {
        var ptype= this.paln
        if(ptype === 1){
          this.$refs.WandanPlanXgModel.wandanplanmodelShow(record)
        }
        if(ptype === 2){
          this.$refs.WandanPlanXg2Model.wandanplanmodelShow(record)
        }
        if(ptype === 3){
          this.$refs.WandanPlanXg3Model.wandanplanmodelShow(record)
        }
        if(ptype === 4){
          this.$refs.WandanPlanXg4Model.wandanplanmodelShow(record)
        }
        this.$refs.WandanPlanXgModel.title = '完单-修改'
      },
      /**
       * 派单类型 change 方法[待定功能]============
       * @Param value 派单类型[0出库/1入库]
       */
      changeOperatorSchema(value) {
        console.log('派单类型[0出库/1入库]', value)
        if (value == 0) {
          // 出库操作
          this.sendOrderType = true
        }
        if (value == 1) {
          // 入库操作
          this.sendOrderType = false
        }
      },
      getUrlNewView(textUrl) {
        let config = window._CONFIG['domianURL'] + '/sys/common/view'
        window.open(config + '/' + textUrl, '_blank')
      },
      queryCler() {
        this.model = {}
        this.form = this.$form.createForm(this)

      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        console.log('分页')
        console.log(pagination)
        console.log(filters)
        console.log(sorter)
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination = pagination;
        this.dataas()
      },
      sdhandleDelete: function (record) {
        let that = this
        /*let param = {
          SendOrdersVo: record,
          id: record.id,
          type: record.planType,
          tableId: this.paln
        }*/
        console.log('完单记录删除操作', record)
        httpAction('/cable/sendOrders/wddelete/' + this.paln, record, 'put').then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.dataas(that.vadjsoifjweoi)
          } else {
            that.$message.warning(res.message)
          }
          that.$emit('ok')
        })
      },
      dataas(ids, paln) {
        let param = {
          ids: this.ids,
          planType: this.paln,
          pageNo: this.ipagination.current,
          pageSize: this.ipagination.pageSize,
        }
        console.log('参数this.ids', this.ids)
        console.log('参数this.paln', this.paln)
        console.log('参数', param)
        getAction('/cable/sendOrders/selectSendOrdersWD', param).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
            console.log('查询数据》：', this.dataSource)
          }
        })
      },
      dakpd(ids, paln) {
        console.log("打开完单记录页面：：", ids, paln)
        this.ids = ids
        this.paln = paln
        this.dataas(ids, paln)
        this.cloer()
        this.visible = true
      },
      close() {
        this.model = {}
        this.form.resetFields()
        this.$emit('close')
        this.visible = false
      },
      cloer() {
        this.form = this.$form.createForm(this)
        console.log('cloer')
        this.popupCallback({})
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            this.model.planId = this.data.id
            this.model.planType = this.paln
            this.model.serial = this.serials
            this.model.projectNo = this.data.projectNo
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              // that.close()
              that.dataas(that.vadjsoifjweoi)
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'operatorSchema', 'warehouseId', 'storageLocation', 'taskTime', 'license', ''))
      }
    }
  }
</script>
<style>
  .ant-table-body {
    overflow: auto;
  }
</style>