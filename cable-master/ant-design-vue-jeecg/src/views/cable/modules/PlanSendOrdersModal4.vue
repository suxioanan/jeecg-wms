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
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 12 }">

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="派单类型">
              <a-select v-decorator="['operatorSchema',validatorRules.operatorSchema]" placeholder="请选择派单类型" @change="changeOperatorSchema">
                <template v-for="(types,index) in sendOrdersTypes">
                  <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="自家仓库">
              <a-select v-decorator="['warehouseId',validatorRules.warehouseId]" @change="types" placeholder="请选择目标仓库">
                <template v-for="warehouse in warehouseLists">
                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12" v-if="this.sendOrderType">
            <a-form-item label="自家库位">
              <a-select v-decorator="['storageLocationId',validatorRules.storageLocationId]" id="sl"
                        placeholder="请选择库位">
                <template v-for="storageLocation in storageLocations">
                  <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}
                  </a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <!-- 出库才选择终点仓库,入库不需要选择终点仓库[根据 this.sendOrderType 做判断是否显示] -->
          <a-col :span="12" v-if="this.sendOrderType">
            <a-form-item label="终点仓库">
              <a-select v-decorator="['endWarehouseId']" placeholder="请选择终点仓库">
                <template v-for="warehouse in warehouseLists">
                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="员工">
              <a-select
                mode="multiple"
                placeholder="请选择员工"
                optionFilterProp="children"
                v-decorator="['realname',validatorRules.realname]"
                :getPopupContainer="(target) => target.parentNode">
                <a-select-option v-for="(role,roleindex) in users" :key="roleindex.toString()" :value="role.id">
                  {{ role.realname }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="车辆">
              <a-select
                mode="multiple"
                placeholder="请选择车辆"
                optionFilterProp="children"
                v-decorator="['license',validatorRules.license]"
                :getPopupContainer="(target) => target.parentNode">
                <a-select-option v-for="(role,roleindex) in vehicles" :key="roleindex.toString()" :value="role.license">
                  {{ role.license }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="任务日期">
              <j-date v-decorator="['taskTime',validatorRules.taskTime]" :showTime="true" date-format="YYYY-MM-DD"
                      placeholder="请选择任务时间">
              </j-date>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-button @click="handleOk" type="primary" style="width: 100px;margin-left: 30px;">
              <template v-if="!model.id">派单</template>
              <template v-if="model.id">保存</template>
            </a-button>
            <a-button @click="queryCler" type="primary" style="width: 100px;margin-left: 30px;">
              清空
            </a-button>
          </a-col>
        </a-row>

        <hr/>
        <div style="">
          <div style="margin-top: 10px;">
            <p>历史记录</p>
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
                <span slot="realname" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="action" slot-scope="text, record">
                   <a-popconfirm title="确定要删除吗?" @confirm="() => sdhandleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
                  <a-divider type="vertical"/>
                   <a @click="handleEdits(record)">编辑</a>
                   <a-divider type="vertical"/>
                   <a @click="wand(record)">完单</a>
                </span>
              </a-table>
            </div>
          </div>

        </div>

      </a-form>
    </a-spin>

    <plan-accomplish-modal4 ref="planAccomplishModal4"></plan-accomplish-modal4>  <!-- 计划4完单modal -->

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
  import { httpAction, getAction, deleteAction } from '@/api/manage'
  import PlanAccomplishModal4 from './PlanAccomplishModal4'
  import '@/assets/less/TableExpand.less'

  export default {
    name: 'PlanSwndOrdersModal4',
    components: {
      JDate,
      JEllipsis,
      PlanAccomplishModal4
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
            title: '目标仓库',
            align: 'center',
            dataIndex: 'warehouseId',
            customRender: (value, row, index) => {
              var s = ''
              for (let item of this.warehouseLists) {
                if (value == item.id) {
                  s = item.name
                }
              }
              return s
            }
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '任务详情',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '联系人',
            align: 'center',
            dataIndex: 'linkman',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '联系电话',
            align: 'center',
            dataIndex: 'phone',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '地址',
            align: 'center',
            dataIndex: 'address',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '参与员工',
            align: 'center',
            dataIndex: 'realname',
            width: 200,
            customRender: (value, row, index) => {
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

            },
            scopedSlots: { customRender: 'realname' }
          },
          {
            title: '安排车辆',
            align: 'center',
            dataIndex: 'a0',
            width: 200,
            customRender(text) {

              if (text == null || text.length == 0) {
                return '未安排车辆'
              }
              return text.replace(',', '、')
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            width: 140,
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        form: this.$form.createForm(this),
        title: '操作',
        width: 1300,
        visible: false,
        model: {},
        confirmLoading: false,
        validatorRules: {
          operatorSchema: { rules: [{ required: true, message: '请选择派单类型' }] },
          warehouseId: { rules: [{ required: true, message: '请选择目标仓库' }] },
          storageLocationId: { rules: [{ required: true, message: '请选择库位' }] },
          taskTime: { rules: [{ required: true, message: '请选择任务时间' }] }
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
    },
    methods: {
      /**
       * 派单类型 change 方法
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
      /**
       * 完单
       * @param val
       */
      wand(val) {
        this.$refs.planAccomplishModal4.dakwd(this.data, this.paln, val.id, val)
        this.$refs.planAccomplishModal4.title = ''
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
        /*this.ipagination = pagination;*/
        this.data(this.date)
      },
      handleEdits(record) {
        if (record.id) {
          record.operatorSchema += ''
          // record.license=record.a0
          // record.realname=record.a1
          this.visiblekey = true
        } else {
          this.visiblekey = false
        }
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'id', 'operatorSchema', 'warehouseId', 'storageLocationId', 'taskTime', 'realname', 'license'))
        })
      },
      sdhandleDelete: function(id) {
        var that = this
        deleteAction(that.url.deleteWangda, { id: id }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.dataas(that.vadjsoifjweoi)
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
      dataas(val) {
        console.log('参数', val)
        getAction('/cable/sendOrders/selectSendOrdersController', val).then((res) => {
          // this.messageList = res.result;
          this.dataSource = res.result.records
          console.log('参数', this.dataSource)
        })
      },
      types(val) {
        this.storageLocations = []
        this.form.setFieldsValue(pick({ storageLocationId: undefined }, 'storageLocationId'))
        let va = val
        getAction('/cable/storageLocation/list', { warehouseId: va }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log(this.storageLocations)
          }
        })
      },
      dakpd(val, paln) {
        console.log("dakpd《《《《《《《《♥》》》》》》》》》:2")
        var dsjoi = val.id
        this.vadjsoifjweoi = { planId: dsjoi, planType: paln }
        this.dataas(this.vadjsoifjweoi)
        this.cloer()
        this.data = val
        this.paln = paln
        if (paln == 1) {
          this.serials = val.wasteMaterialCode
        } else if (paln == 2) {
          this.serials = val.receiptNo
        } else if (paln == 3) {
          this.serials = val.materialCode
        } else {
          //TODO
          this.serials = 'cable2'
        }
        this.warehouseList()
        this.sendOrdersType()
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
            this.model.assetNo = this.data.assetNo
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