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
                rowKey="id"
                size="middle"
                bordered
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                @change="handleTableChange"
                class="j-table-force-nowrap">
                <span slot="factoryText" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="factoryText7" slot-scope="text">
                  <j-ellipsis :value="text" :length="7"/>
                </span>
                <span slot="action" v-has="'orderJL:action'" slot-scope="text,record">
                  <a-popconfirm v-has="'orderJL:del'" title="确定要删除吗?" @confirm="() => sdhandleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                    <a-divider type="vertical"/>
                  <a v-has="'orderJL:edit'" @click="mergePlanEdit(record)">编辑</a>
                </span>
              </a-table>
            </div>
          </div>

        </div>

      </a-form>
    </a-spin>

    <plan-accomplish-modal ref="planAccomplishModal"></plan-accomplish-modal>
    <plan-the-same-day-de-modal ref="planTheSameDayDeModal" @ok="modalFormOk"></plan-the-same-day-de-modal>
    <merge-plan-model-plan1-edit ref="MergePlanModelPlan1Edit" @ok="modalFormOk"></merge-plan-model-plan1-edit><!-- 派单编辑 -->
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
    import MergePlanModelPlan1Edit from "./MergePlanModelPlan1Edit";

    export default {
        name: 'PlanSendOrdersJLModal',
        components: {
          MergePlanModelPlan1Edit,
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
                        dataIndex: 'operatorSchema',
                        width: 80,
                        customRender: (value, row, index) => {
                            var s = ''
                            for (let item of this.sendOrdersTypes) {
                                if (value == item.itemValue) {
                                    s = item.itemText
                                }
                            }
                            return s
                        }
                    },
                    {
                        title: '任务日期',
                        align: 'center',
                        dataIndex: 'taskTime',
                        width: 90
                    },
                    {
                        title: '自家仓库',
                        align: 'center',
                        dataIndex: 'warehouseName',
                      scopedSlots: {customRender: 'factoryText'}
                        /*customRender: (value, row, index) => {
                            var s = ''
                            for (let item of this.warehouseLists) {
                                if (value == item.id) {
                                    s = item.name
                                }
                            }
                            return s
                        }*/
                    },
                    {
                        title: '自家库位',
                        align: 'center',
                        dataIndex: 'storageLocationName',
                        scopedSlots: {customRender: 'factoryText'}
                    },
                    {
                        title: '终点仓库',
                        align: 'center',
                        dataIndex: 'endWarehouseName',
                      scopedSlots: {customRender: 'factoryText'}
                        /*customRender: (value, row, index) => {
                            var s = ''
                            for (let item of this.warehouseLists) {
                                if (value == item.id) {
                                    s = item.name
                                }
                            }
                            return s
                        }*/
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
                        title: '派单数量',
                        align: 'center',
                        dataIndex: 'backup1',
                    },
                    {
                        title: '联系人',
                        align: 'center',
                        dataIndex: 'linkman',
                        scopedSlots: {customRender: 'factoryText'}
                    },
                    {
                        title: '联系电话',
                        align: 'center',
                        dataIndex: 'phone',
                        scopedSlots: {customRender: 'factoryText'}
                    },
                    {
                        title: '地址',
                        align: 'center',
                        dataIndex: 'address',
                        scopedSlots: {customRender: 'factoryText'}
                    },
                    {
                        title: '参与员工',
                        align: 'center',
                        dataIndex: 'a1',
                        width: 200,
                      scopedSlots: {customRender: 'factoryText7'}
                      /*customRender: (value, row, index) => {
                        var s = ''
                        for (let item of this.users) {
                          for (let val of value) {
                            if (val == item.id) {
                              s += item.realname + '、'
                            }
                          }
                        }
                        if (s.length > 0) {
                          s = s.substring(0, s.length - 1)
                          return s
                        } else {
                          return '未安排人员'
                        }

                      },*/
                    },
                    {
                        title: '安排车辆',
                        align: 'center',
                        dataIndex: 'a0',
                        width: 200,
                      scopedSlots: {customRender: 'factoryText7'}
                        /*customRender(text) {
                            if (text == null || text.length == 0) {
                                return '未安排车辆'
                            }
                            return text.replace(',', '、')
                        }*/
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
                width: 1400,
                visible: false,
                model: {},
                confirmLoading: false,
                validatorRules: {
                    operatorSchema: {rules: [{required: true, message: '请选择派单类型'}]},
                    warehouseId: {rules: [{required: true, message: '请选择目标仓库'}]},
                    endWarehouseId: {rules: [{required: true, message: '请选择终点仓库'}]},
                    storageLocationId: {rules: [{required: true, message: '请选择库位'}]},
                    taskTime: {rules: [{required: true, message: '请选择任务时间'}]}
                },
                url: {
                    add: '/cable/sendOrders/add',
                    deleteWangda: '/cable/sendOrders/delete',
                    edit: '/cable/sendOrders/edit'
                },
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
            this.warehouseList()
            this.sendOrdersType()
            this.vehiclesList()
            this.usersList()
          //权限
            this.disableMixinCreated=true;
            this.columns = colAuthFilter(this.columns,'orderJL:');
            this.loadData();
            this.initDictConfig();
        },
        methods: {
          /**
           * 派单编辑
           */
          mergePlanEdit(record) {
            console.log("点击了派单编辑",record)
            //TODO 打开合并派单页面
            this.$refs.MergePlanModelPlan1Edit.mergePlanModelEditShow(record)
            this.$refs.MergePlanModelPlan1Edit.title = '派单编辑'
          },
          modalFormOk() {
            // 新增/修改 成功时，重载列表
            this.dataas()
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
            sdhandleDelete: function (id) {
                var that = this
                deleteAction(that.url.deleteWangda, {id: id}).then((res) => {
                    if (res.success) {
                        that.$message.success(res.message)
                        that.dataas()
                    } else {
                        that.$message.warning(res.message)
                    }
                    that.$emit('ok')
                })
            },
            usersList() {
                this.users = ''
                getAction('/cable/plan2/queryUserList').then((res) => {
                    if (res.success) {
                        this.users = res.result
                        console.log(this.users)
                    }
                })
            },
            vehiclesList() {
                this.vehicles = ''
                getAction('/cable/vehicle/selectVehicleList').then((res) => {
                    if (res.success) {
                        this.vehicles = res.result
                        console.log(this.vehicles)
                    }
                })
            },
            dataas() {
              console.log('参数', this.vadjsoifjweoi)
              let param = {
                ids:this.vadjsoifjweoi.ids,
                planType:this.vadjsoifjweoi.planType,
                pageNo:this.ipagination.current,
                pageSize:this.ipagination.pageSize,
              }
              getAction('/cable/sendOrders/selectSendOrdersController', param).then((res) => {
                this.dataSource=res.result.records
                this.ipagination.total = res.result.total
              })
              console.log('参数', this.dataSource)
            },
            types(val) {
                this.storageLocations = []
                this.form.setFieldsValue(pick({storageLocationId: undefined}, 'storageLocationId'))
                let va = val
                getAction('/cable/storageLocation/list', {warehouseId: va}).then((res) => {
                    if (res.success) {
                        this.storageLocations = res.result
                        console.log(this.storageLocations)
                    }
                })
            },
            dakpd(ids, planType) {
              console.log("打开派单记录页面：：",ids,planType)
                this.vadjsoifjweoi = {ids: ids, planType: planType}
                this.dataas()
                this.cloer()
                // this.data = val
                // console.log(val)
                // console.log("dakpd《《《《《《《《♥》》》》》》》》》:2")
                // this.paln = paln
                // // 不同计划派单所对应的物料编号
                // if (paln == 1) {
                //     // 计划1物料编号
                //     this.serials = val.wasteMaterialCode
                // } else if (paln == 2) {
                //     // 计划2物料编号
                //     this.serials = val.receiptNo
                //     // this.serials = val.equipmentNo
                // } else if (paln == 3) {
                //     // 计划3物料编号
                //     this.serials = val.materialCode
                // } else {
                //     // 计划4物料编号
                //     this.serials = 'cable2'
                // }
                // this.warehouseList()
                // this.sendOrdersType()
                this.visible = true
            },
            warehouseList() {
                this.warehouseLists = ''
                this.storageLocations = ''
                getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
                    if (res.success) {
                        this.warehouseLists = res.result
                    }
                })
            },
            sendOrdersType() {
                this.sendOrdersTypes = ''
                getAction('/sys/dictItem/getDictItemSendOrdersType').then((res) => {
                    if (res.success) {
                        console.log(res)
                        console.log('派单类型')
                        this.sendOrdersTypes = res.result
                    }
                })
            },
            close() {
                this.model = {}
                this.form.resetFields()
                this.$emit('close')
                this.visible = false
            },
            cloer() {
                this.form = this.$form.createForm(this)
                // this.model.operatorSchema='';
                // this.sendOrdersTypes={}
                // this.storageLocations={}
                // this.vehicles={}
                // this.users={}
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
                            that.dataas()
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