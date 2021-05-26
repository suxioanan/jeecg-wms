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
              <a-select v-decorator="['warehouseId',validatorRules.warehouseId]" @change="types" placeholder="请选择自家仓库">
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
                        placeholder="请选择自家库位">
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
            <a-button @click="handleOk" type="primary" style="width: 100px;float: right;margin-right: 230px">
              <template v-if="!model.id">派单</template>
              <template v-if="model.id">保存</template>
            </a-button>
            <a-button @click="queryCler" type="primary" style="width: 100px;float:right;margin-right: 30px;">
              清空
            </a-button>
          </a-col>
        </a-row>

        <hr/>
        <div style="">
          <div style="margin-top: 10px;">
            <p>任务详情</p>
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
                <!-- 工程名称，不同计划，值不同 -->
                <span slot="proName" slot-scope="text, record">
                  <j-ellipsis :value="text" :length="10"/>
                  <span v-if="record.planType == '配变电'">{{record.projectName}}</span>
                  <span v-else-if="record.planType == '备品'">{{record.site}}</span>
                  <span v-else-if="record.planType == '新品'">{{record.engName}}</span>
                  <span v-else-if="record.planType == '临措'">{{record.engName}}</span>
                  <span v-else-if="record.planType == '电缆'">{{record.engName}}</span>
                </span>

                <span slot="factoryText" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="realname" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="action" slot-scope="text, record">
                  <a-input-number @change="onChange"></a-input-number>
                </span>
              </a-table>
            </div>
          </div>

        </div>

      </a-form>
    </a-spin>

    <plan-accomplish-modal ref="planAccomplishModal"></plan-accomplish-modal>

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

  export default {
    //合并完单页面
    name: 'CompletePlanModel',
    components: {
      JDate,
      JEllipsis,
      PlanAccomplishModal
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
        ids: '',
        options:[],
        columns: [
          {
            title: '工程账号',
            align: 'center',
            dataIndex: 'projectNo',
          },
          {
            title: '工程名称',
            align: 'center',
            width: 150,
            dataIndex: 'projectName',
            customRender(text,record) {
              if (record.planType == '配变电') {
                return record.projectName
              }else if (record.planType == '备品') {
                return record.site
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.engName
              }else if (record.planType == '电缆') {
                return record.engName
              }
            },
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'wasteMaterialText',
            customRender(text,record) {
              if (record.planType == '配变电') {
                if(null != record.rawMaterialText){
                  //原物料描述
                  return record.rawMaterialText
                }else if(null != record.wasteMaterialText){
                  //旧物料描述
                  return record.wasteMaterialText
                }
              }else if (record.planType == '备品') {
                return record.equipmentName
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.materialDescribe
              }else if (record.planType == '电缆') {
                return record.cableName
              }
            },
          },
          {
            title: '物料数量',
            align: 'center',
            dataIndex: 'numReceipts',
            customRender(text,record) {
              if (record.planType == '配变电') {
                return record.numReceipts
              }else if (record.planType == '备品') {
                return record.capacity
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.num
              }else if (record.planType == '电缆') {
                return record.samplingLength
              }
            },
          },
          {
            title: '地址',
            align: 'center',
            dataIndex: 'engineeringAddress',
            customRender(text,record) {
              if (record.planType == '配变电') {
                return record.engineeringAddress
              }else if (record.planType == '备品') {
                return record.address
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.address
              }else if (record.planType == '电缆') {
                return record.samplingAddres
              }
            },
          },
          {
            title: '联系人',
            align: 'center',
            dataIndex: 'projectContact',
            customRender(text,record) {
              if (record.planType == '配变电') {
                return record.projectContact
              }else if (record.planType == '备品') {
                return record.equipmentOwners
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.projectManager
              }else if (record.planType == '电缆') {
                return record.teamContact
              }
            },
          },
          {
            title: '联系电话',
            align: 'center',
            dataIndex: 'projectPhone',
            customRender(text,record) {
              if (record.planType == '配变电') {
                return record.projectPhone
              }else if (record.planType == '备品') {
                return ""
              }else if (record.planType == '新品' || record.planType == '临措') {
                return record.mPhone
              }else if (record.planType == '电缆') {
                return ""
              }
            },
          },
          {
            title: '操作',
            dataIndex: 'action',
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
          operatorSchema: {rules: [{required: true, message: '请选择派单类型'}]},
          warehouseId: {rules: [{required: true, message: '请选择自家仓库'}]},
          endWarehouseId: {rules: [{required: true, message: '请选择终点仓库'}]},
          storageLocationId: {rules: [{required: true, message: '请选择库位'}]},
          taskTime: {rules: [{required: true, message: '请选择任务时间'}]}
        },
        url: {
          add: '/cable/sendOrders/mergePlanadd',
          deleteWangda: '/cable/sendOrders/delete',
          edit: '/cable/sendOrders/edit',
          list1: '/cable/plan1/idslist',
          list2: '/cable/plan2/idslist',
          list3: '/cable/plan3/idslist',
          list4: '/cable/plan4/idslist',
        },
        paln: '',
        data: {},
        vehicles: {},
        users: {},
        serials: '',
        storageLocations2: [],
        vadjsoifjweoi: {},
        // 派单类型
        sendOrderType: false,
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

      onChange(value) {
        console.log('changed当前值：', value);
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
      wand(val) {
        console.log('PlanSendOrdersModal 完单wan', val)
        this.$refs.planAccomplishModal.dakwd(this.data, this.paln, val.id, val)
        this.$refs.planAccomplishModal.title = ''
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
          this.form.setFieldsValue(pick(this.model, 'id', 'operatorSchema', 'warehouseId', 'endWarehouseId', 'storageLocationId', 'taskTime', 'realname', 'license'))
        })
        //TODO 打开编辑调用自家仓库change事件，查询级联下拉自家库位
        this.types(record.warehouseId);
      },
      sdhandleDelete: function (id) {
        var that = this
        deleteAction(that.url.deleteWangda, {id: id}).then((res) => {
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
        this.users = {}
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
        this.form.setFieldsValue(pick({storageLocationId: undefined}, 'storageLocationId'))
        let va = val
        getAction('/cable/storageLocation/list', {warehouseId: va}).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log(this.storageLocations)
          }
        })
      },
      completePlanModelShow(ids,paln) {
        this.ids = ids.toString()
        this.paln = paln
        let url = '';
        if(paln == '1') { url = this.url.list1 }
        else if(paln == '2'){ url += this.url.list2}
        else if(paln == '3'){ url += this.url.list3}
        else if(paln == '4'){ url += this.url.list4}

        console.log(paln,"进入了合并派单页面》》》", ids.toString(),"url地址",url)
          getAction(url, {ids: ids.toString()}).then((res) => {
          if (res.success) {
            this.dataSource = res.result
            console.log(res.result)
          }
        })
        this.visible = true
        return

        this.cloer()
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
        //TODO 派单提交触发表单验证
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
            //计划id
            this.model.planId = this.data.id
            //计划类型
            this.model.planType = this.paln
            //物料编码
            this.model.serial = this.serials
            //工程编号
            this.model.projectNo = this.data.projectNo
            //ids 计划id的集合（字符类型）
            this.model.ids = this.ids
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
              // that.dataas(that.vadjsoifjweoi)
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