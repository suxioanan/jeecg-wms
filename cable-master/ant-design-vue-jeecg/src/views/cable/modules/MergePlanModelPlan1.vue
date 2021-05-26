<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form"
              :label-col="{ span: 8 }"
              :wrapper-col="{ span: 14 }">
        <!-- 主表单区域 -->

        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="派单类型">
              <a-select v-decorator="['operatorSchema',validatorRules.operatorSchema]" placeholder="请选择派单类型" @change="changeOperatorSchema">
                <a-select-option value="0">出库</a-select-option>
                <a-select-option value="1">入库</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="任务日期">
              <j-date v-decorator="['taskTime',validatorRules.taskTime]" :showTime="true" date-format="YYYY-MM-DD"
                      placeholder="请选择任务时间" style="width: 80%">
              </j-date>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="员工">
              <j-multi-select-tag
                v-decorator="['realname',validatorRules.realname]"
                :options="dictOptions"
                dictCode="sys_user,realname,id,status='1' and username!='admin'"
                placeholder="请做出你的选择">
              </j-multi-select-tag>

            </a-form-item>
          </a-col>

        </a-row>

        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
            <a-form-item label="联系人">
              <a-input placeholder="请输入联系人"
                       v-decorator="['backup3',validatorRules.backup3]"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12">
            <a-form-item label="联系电话">
              <a-input placeholder="请输入联系电话"
                       v-decorator="['backup4',validatorRules.backup4]"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12">
            <a-form-item label="任务地址">
              <a-input placeholder="请输入任务地址"
                       v-decorator="['backup2',validatorRules.backup2]"></a-input>
            </a-form-item>
          </a-col>

        </a-row>
        <!--<a-row :gutter="24">
          &lt;!&ndash;<a-row :gutter="24">
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
          </a-row>&ndash;&gt;

          <a-col :span="12">
            <a-button @click="handleOk" type="primary" style="width: 100px;float: right;margin-right: 230px">
              <template v-if="!model.id">派单</template>
              <template v-if="model.id">保存</template>
            </a-button>
            <a-button @click="queryCler" type="primary" style="width: 100px;float:right;margin-right: 30px;">
              清空
            </a-button>
          </a-col>
        </a-row>-->
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="派单信息" key="1" :forceRender="true">

          <j-editable-table
            ref="editableTable1"
            :loading="table1.loading"
            :columns="table1.columns"
            :dataSource="table1.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :alwaysEdit="true"
            :rowSelection="true"
            :actionButton="true"/>

        </a-tab-pane>

        <a-tab-pane tab="派单车辆信息" key="2" :forceRender="true">

          <j-editable-table
            ref="editableTable2"
            :loading="table2.loading"
            :columns="table2.columns"
            :dataSource="table2.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :rowSelection="true"
            :actionButton="true"
            :addButton="true"
          />

          <span slot="factoryText" slot-scope="text">
            <j-ellipsis :value="text" :length="10"/>
          </span>

        </a-tab-pane>
      </a-tabs>

    </a-spin>

  </a-modal>

</template>

<script>

  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes, VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { httpAction, getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import pick from 'lodash.pick'
  import moment from 'moment'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JMultiSelectTag from '@/components/dict/JMultiSelectTag'

  export default {
    // plan1 合并派单页面
    name: 'MergePlanModelPlan1',
    components: {
      JDate, JEditableTable, JSearchSelectTag, JMultiSelectTag
    },
    data() {
      return {
        title: '操作',
        visible: false,
        form: this.$form.createForm(this),
        confirmLoading: false,
        // 派单类型
        sendOrderType: false,
        // 人员集合
        dictOptions:[],
        validatorRules: {
          operatorSchema: {rules: [{required: true, message: '请选择派单类型'}]},
          warehouseId: {rules: [{required: true, message: '请选择自家仓库'}]},
          endWarehouseId: {rules: [{required: true, message: '请选择终点仓库'}]},
          storageLocationId: {rules: [{required: true, message: '请选择库位'}]},
          taskTime: {rules: [{required: true, message: '请选择任务时间'}]}
        },
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 - 6 }
        },
        activeKey: '1',
        // 默认table
        table1: {
          loading: false,
          dataSource: [],
          columns:  [

          ],
        },
        // 入库派单信息
        tableRu: {
          loading: false,
          dataSource: [],
          columns:  [
            {
              title: '工程账号',
              key: 'projectNo',
              width: '12%',
              type: FormTypes.normal,
              defaultValue: 'A1002',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '工程名称',
              key: 'projectName',
              width: '22%',
              type: FormTypes.normal,
              defaultValue: 'A一零零二',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }],
              scopedSlots: { customRender: 'factoryText' },
            },
            {
              title: '物料名称',
              key: 'wasteMaterialText',
              width: '20%',
              type: FormTypes.normal,
              defaultValue: 'A02',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '派单数量',
              key: 'backup1',
              width: '8%',
              type: FormTypes.inputNumber,
              defaultValue: '1',
              placeholder: '${title}',
              validateRules: [{ required: true,pattern: /^([^0]|.{2,})$/, message: '${title}不能为空' }]
            },
            /*{
              title: '自家仓库',
              key: 'warehouseId',
              width: '10%',
              type: FormTypes.select,
              dictCode:"warehouse,name,id,type='1'",
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }],
              onchange: 'onchange'
            },
            {
              title: '自家库位',
              key: 'storageLocationId',
              width: '10%',
              type: FormTypes.select,
              validateRules: [{ required: false, message: '请选择${title}' }],
              placeholder: '请选择${title}'
            },
            {
              title: '终点仓库',
              key: 'endWarehouseId',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'warehouse,name,id',
              placeholder: '请选择${title}',
              validateRules: [{ required: false, message: '请选择${title}' }]
            },
            {
              title: '联系人',
              key: 'backup3',
              width: '12%',
              type: FormTypes.input,
              defaultValue: '李四',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '联系电话',
              key: 'backup4',
              width: '12%',
              type: FormTypes.input,
              defaultValue: '10086',
              placeholder: '请输入${title}',
              validateRules: [{ required: false,pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的${title}' }]
            },
            {
              title: '任务地址',
              key: 'backup2',
              width: '14%',
              type: FormTypes.input_pop,
              defaultValue: '长江南路电力仓库',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },*/
          ],
        },
        // 出库派单信息
        tableChu: {
          loading: false,
          dataSource: [],
          columns:  [
            {
              title: '工程账号',
              key: 'projectNo',
              width: '12%',
              type: FormTypes.normal,
              defaultValue: 'A1002',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '工程名称',
              key: 'projectName',
              width: '22%',
              type: FormTypes.normal,
              defaultValue: 'A一零零二',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }],
              scopedSlots: { customRender: 'factoryText' },
            },
            {
              title: '物料名称',
              key: 'wasteMaterialText',
              width: '20%',
              type: FormTypes.normal,
              defaultValue: 'A02',
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '自家仓库',
              key: 'wname',
              width: '10%',
              type: FormTypes.normal,
              // disabled:true,
              // dictCode:"warehouse,name,id",
              placeholder: '请选择${title}',
              validateRules: [{ required: false, message: '请选择${title}' }],
              // onchange: 'onchange'
            },
            {
              title: '自家库位',
              key: 'storagename',
              width: '10%',
              type: FormTypes.normal,
              // disabled:true,
              // dictCode: 'storage_location,storage_location_name,id',
              validateRules: [{ required: false, message: '请选择${title}' }],
              placeholder: '请选择${title}'
            },
            {
              title: '可出库数量',
              key: 'inventoryQuantity',
              width: '8%',
              type: FormTypes.normal,
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '${title}不能为空' }]
            },
            {
              title: '派单数量',
              key: 'backup1',
              width: '8%',
              type: FormTypes.inputNumber,
              defaultValue: '1',
              placeholder: '${title}',
              validateRules: [{ required: true,pattern: /^([^0]|.{2,})$/, message: '${title}不能为空' }]
            },
            {
              title: '终点仓库',
              key: 'endWarehouseId',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'warehouse,name,id',
              placeholder: '请选择${title}',
              validateRules: [{ required: false, message: '请选择${title}' }]
            }
          ],
        },
        // 派单车辆信息
        table2: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '车辆',
              key: 'license',
              width: '15%',
              type: FormTypes.sel_search,
              dictCode:"vehicle,license,license,state='0'",
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '数量',
              key: 'number',
              width: '6%',
              type: FormTypes.inputNumber,
              defaultValue: '1',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            /*{
              title: '员工',
              key: 'realname',
              width: '20%',
              type: FormTypes.sel_search,
              options:this.vehicles,
              dictCode:"sys_user,realname,id,status='1'",
              defaultValue: '',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },*/
          ]
        },
        urlChu: {
          list1: '/cable/plan1/idslistChu',
          list2: '/cable/plan2/idslistChu',
          list3: '/cable/plan3/idslistChu',
          list4: '/cable/plan4/idslistChu',
        },
        urlRu: {
          list1: '/cable/plan1/idslistRu',
          list2: '/cable/plan2/idslistRu',
          list3: '/cable/plan3/idslistRu',
          list4: '/cable/plan4/idslistRu',

        },
        url:{
          add: '/cable/sendOrders/mergePlanadd',
          edit: '/cable/sendOrders/mergePlanadd',
          orderCustomerList: '/test/jeecgOrderMain/queryOrderCustomerListByMainId',
          orderTicketList: '/test/jeecgOrderMain/queryOrderTicketListByMainId'
        }
      }
    },
    created() {

    },
    methods: {
      onchange(val){
        console.log("进入了sendNumberonblur方法！！！")
        console.log("对象的id >>>>:",val)
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
          this.table1 = this.tableChu
        }
        if (value == 1) {
          // 入库操作
          this.sendOrderType = false
          this.table1 = this.tableRu
        }
      },
      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableTable1'),
          getRefPromise(this, 'editableTable2')
        ])
      },

      add() {
        // 默认新增一条数据
        this.getAllTable().then(editableTables => {
          editableTables[0].add()
          editableTables[1].add()
        })

        this.edit({})
      },
      mergePlanModelShow(ids,paln) {
        // 默认新增一条数据
        this.getAllTable().then(editableTables => {
          editableTables[1].add()
        })
        this.activeKey = '1'

        this.ids = ids.toString()
        this.paln = paln
        let urlRu = '';
        let urlChu = '';
        if(paln == '1') {
          urlRu = this.urlRu.list1
          urlChu = this.urlChu.list1
        }
        else if(paln == '2'){
          urlRu += this.urlRu.list2
          urlChu += this.urlChu.list2
        }
        else if(paln == '3'){
          urlRu += this.urlRu.list3
          urlChu += this.urlChu.list3
        }
        else if(paln == '4'){
          urlRu += this.urlRu.list4
          urlChu += this.urlChu.list4
        }


        console.log("打开合并派单页面》》》： ",ids)
        // 加载子表数据
        if (ids) {
          let params = { ids: this.ids }
          this.requestTableData(urlRu, params, this.tableRu)
          this.requestTableData(urlChu, params, this.tableChu)
        }
      },
      close() {

        console.log("进入 close 方法》》》》》")
        this.visible = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
          editableTables[1].initialize()
        })
        this.$emit('close')
      },
      /** 查询某个tab的数据 */
      requestTableData(url, params, tab) {
        console.log("进入查询方法》》》》》",url, params, tab)
        tab.loading = true
        getAction(url, params).then(res => {
          if (res.success) {
            this.visible = true
            tab.dataSource = res.result
          }else {
            this.visible = false
            this.$message.warning(res.message)
            return
          }
          console.log("查询成功》》数据是》》：",tab.dataSource,"表格是》：",tab)
        }).finally(() => {
          tab.loading = false
          this.form.setFieldsValue({operatorSchema:'1'})
          this.table1 = this.tableRu
        })
      },
      handleOk() {
        this.validateFields()
      },
      handleCancel() {
        this.close()
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        getRefPromise(this, `editableTable${key}`).then(editableTable => {
          editableTable.resetScrollTop()
        })
      },

      /** 触发表单验证 */
      validateFields() {
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)

          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let orderMain = Object.assign(this.model, allValues.formValue)
        //时间格式化
        // orderMain.orderDate = orderMain.orderDate ? orderMain.orderDate.format('YYYY-MM-DD HH:mm:ss') : null
        return {
          ...orderMain, // 展开
          jeecgOrderCustomerList: allValues.tablesValue[0].values,
          jeecgOrderTicketList: allValues.tablesValue[1].values
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        formData.planType = this.paln
        if (formData.realname != undefined)
          formData.realname = formData.realname.split(",")


        if (formData.operatorSchema == 0) {
          let backup1 = Number(formData.jeecgOrderCustomerList[0].backup1)
          let inventoryQuantity = Number(formData.jeecgOrderCustomerList[0].inventoryQuantity)
          if (backup1 > inventoryQuantity) return this.$message.warning("出库数量不足！")
        }

        console.log("发起新增或修改的请求",url, formData, method)
        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      }

    }
  }
</script>

<style scoped>
</style>