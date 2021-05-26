<template>
  <a-modal
    :title="title"
    :width="1500"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 父表单 -->
      <a-form :form="form" :label-col="{ span: 8 }" :wrapper-col="{ span: 14 }">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="完单类型">
              <a-select v-decorator="['operatorSchema', validatorRules.operatorSchema]" placeholder="请选择派单类型"
                        @change="switchOperatorSchema">
                <a-select-option value="0">出库</a-select-option>
                <a-select-option value="1">入库</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="任务日期">
              <j-date v-decorator="['taskTime', validatorRules.taskTime]" :showTime="true" date-format="YYYY-MM-DD"
                      placeholder="请选择任务时间">
              </j-date>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="回单">
              <j-upload v-decorator="['receiptPhotos', validatorRules.receipt_photos]" text="点击上传"></j-upload>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="交接单号">
              <a-input v-decorator="['plan4ReceiptNo']" placeholder="请输入交接单号"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <!-- 子表单 -->
      <a-tabs v-model="activeKey">
        <a-tab-pane :tab="tabTitle" key="1" :forceRender="true">
          <j-editable-table
            ref="defaultTable"
            :loading="defaultTable.loading"
            :columns="defaultTable.columns"
            :dataSource="defaultTable.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :alwaysEdit="false"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
        <!--<a-tab-pane tab="入库完单信息" key="2" :forceRender="true">
          <j-editable-table
            ref="editableTable2"
            :loading="table2.loading"
            :columns="table2.columns"
            :dataSource="table2.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>-->
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes, VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { httpAction, getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import pick from 'lodash.pick'
  import JUpload from '@/components/jeecg/JUpload'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: 'CompletePlan1Model',
    mixins: [JeecgListMixin],
    components: {
      JDate,
      JEditableTable,
      JUpload,
      JSearchSelectTag
    },
    data() {
      return {
        title: '操作',
        visible: false,
        form: this.$form.createForm(this),
        confirmLoading: false,
        storageLocations: [],
        model: {},
        validatorRules: {
          operatorSchema: { rules: [{ required: true, message: '请选择完单类型' }] },
          taskTime: { rules: [{ required: true, message: '请选择任务时间' }] },
          receipt_photos: { rules: [{ required: true, message: '请上传回单图片' }] }
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 - 6 }
        },
        // 计划3页面打开合并完单获取到的计划id集
        plan4Ids: '',
        activeKey: '1',
        tabTitle: '入库完单信息',
        defaultTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '项目名称',
              key: 'engName',
              width: '20%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '电缆名称',
              key: 'cableName',
              width: '15%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '电缆规格',
              key: 'cableCross',
              width: '15%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '自家仓库',
              key: 'warehouseName',
              width: '15%',
              type: FormTypes.normal,
              // dictCode: 'warehouse,name,id,type=\'1\'',
              placeholder: '请选择${title}',
              // validateRules: [{ required: true, message: '请选择${title}' }],
              // onchange: 'onchange'
            },
            {
              title: '自家库位',
              key: 'storageLocationName',
              width: '15%',
              type: FormTypes.normal,
              // validateRules: [{ required: true, message: '请选择${title}' }],
              placeholder: '请选择${title}'
            },
            {
              title: '可出库重量',
              key: 'inventoryQuantity',
              width: '15%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '回收规格',
              key: 'recyclingSpecifications',
              width: '15%',
              type: FormTypes.select,
              dictCode: 'recyclingSpecifications',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '材质',
              key: 'texture',
              width: '15%',
              type: FormTypes.select,
              placeholder: '请选择${title}',
              options: [ // 下拉选项
                { title: '铜', value: '铜' },
                { title: '铝', value: '铝' }
              ],
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '完单数量',
              key: 'accomplishNum',
              width: '15%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '请输入${title}' }]
            },
            {
              title: '单位',
              key: 'unit',
              width: '15%',
              type: FormTypes.select,
              dictCode: 'unit',
              placeholder: '请选择${title}',
              validateRules: [{ required: false, message: '请选择${title}' }]
            },
            {
              title: '完单重量',
              key: 'accomplishWeight',
              width: '15%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}，单位：吨',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            // {
            //   title: '重量单位',
            //   key: 'weightUnit',
            //   width: '5%',
            //   type: FormTypes.select,
            //   dictCode: 'unit',
            //   placeholder: '请选择${title}',
            //   validateRules: [{ required: true, message: '请选择${title}' }]
            // },
            {
              title: '完单容积',
              key: 'accomplishVolume',
              width: '15%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            {
              title: '终点仓库',
              key: 'endWarehouseId',
              width: '15%',
              type: FormTypes.select,
              dictCode: 'warehouse,name,id',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '说明',
              key: 'annotation',
              width: '10%',
              type: FormTypes.input_pop,
              placeholder: '请输入${title}'
            }
          ]
        },
        // 出库完单信息
        table1: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '项目名称',
              key: 'engName',
              width: '15%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '电缆名称',
              key: 'cableName',
              width: '10%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            /*{
              title: '电缆规格',
              key: 'cableCross',
              width: '10%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },*/
            {
              title: '电缆规格',
              key: 'cableCross',
              width: '10%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '自家仓库',
              key: 'warehouseName',
              width: '10%',
              type: FormTypes.normal,
              // dictCode: 'warehouse,name,id,type=\'1\'',
              placeholder: '请选择${title}',
              // validateRules: [{ required: true, message: '请选择${title}' }],
              // onchange: 'onchange'
            },
            {
              title: '自家库位',
              key: 'storageLocationName',
              width: '10%',
              type: FormTypes.normal,
              // validateRules: [{ required: true, message: '请选择${title}' }],
              placeholder: '请选择${title}'
            },
            {
              title: '可出库重量',
              key: 'inventoryQuantity',
              width: '8%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            /*{
              title: '回收规格',
              key: 'recyclingSpecifications',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'recyclingSpecifications',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '材质',
              key: 'texture',
              width: '5%',
              type: FormTypes.select,
              placeholder: '请选择${title}',
              options: [ // 下拉选项
                { title: '铜', value: '铜' },
                { title: '铝', value: '铝' }
              ],
              validateRules: [{ required: true, message: '请选择${title}' }]
            },*/
            {
              title: '完单数量',
              key: 'accomplishNum',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: false, message: '请输入${title}' }]
            },
            {
              title: '单位',
              key: 'unit',
              width: '5%',
              type: FormTypes.select,
              dictCode: 'unit',
              placeholder: '请选择${title}',
              validateRules: [{ required: false, message: '请选择${title}' }]
            },
            {
              title: '完单重量',
              key: 'accomplishWeight',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}，单位：吨',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            // {
            //   title: '重量单位',
            //   key: 'weightUnit',
            //   width: '5%',
            //   type: FormTypes.select,
            //   dictCode: 'unit',
            //   placeholder: '请选择${title}',
            //   validateRules: [{ required: true, message: '请选择${title}' }]
            // },
            {
              title: '完单容积',
              key: 'accomplishVolume',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            {
              title: '终点仓库',
              key: 'endWarehouseId',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'warehouse,name,id',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '说明',
              key: 'annotation',
              width: '10%',
              type: FormTypes.input_pop,
              placeholder: '请输入${title}'
            }
          ]
        },
        // 入库完单信息
        table2: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '项目名称',
              key: 'engName',
              width: '15%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '电缆规格',
              key: 'cableCross',
              width: '10%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '电缆名称',
              key: 'cableName',
              width: '10%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '预抽取长度',
              key: 'samplingLength',
              width: '8%',
              type: FormTypes.normal,
              placeholder: '请输入${title}'
            },
            {
              title: '回收规格',
              key: 'recyclingSpecifications',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'recyclingSpecifications',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '材质',
              key: 'texture',
              width: '5%',
              type: FormTypes.select,
              placeholder: '请选择${title}',
              options: [ // 下拉选项
                { title: '铜', value: '铜' },
                { title: '铝', value: '铝' }
              ],
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '完单数量',
              key: 'accomplishNum',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            {
              title: '数量单位',
              key: 'unit',
              width: '5%',
              type: FormTypes.select,
              dictCode: 'unit',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '完单重量',
              key: 'accomplishWeight',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}，单位：吨',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            /*{
              title: '重量单位',
              key: 'weightUnit',
              width: '5%',
              type: FormTypes.select,
              dictCode: 'unit',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }]
            },*/
            {
              title: '完单容积',
              key: 'accomplishVolume',
              width: '8%',
              type: FormTypes.inputNumber,
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '请输入${title}' }]
            },
            {
              title: '自家仓库',
              key: 'warehouseId',
              width: '10%',
              type: FormTypes.select,
              dictCode: 'warehouse,name,id',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '请选择${title}' }],
              onchange: 'onchange'
            },
            {
              title: '库位',
              key: 'storageLocationId',
              width: '10%',
              type: FormTypes.select,
              validateRules: [{ required: true, message: '请选择${title}' }],
              placeholder: '请选择${title}'
            },
            {
              title: '是否异常',
              key: 'sceneSituation',
              width: '8%',
              type: FormTypes.select,
              placeholder: '请选择${title}',
              options: [ // 下拉选项
                { title: '正常', value: '0' },
                { title: '异常', value: '1' }
              ],
              validateRules: [{ required: true, message: '请选择${title}' }]
            },
            {
              title: '异常原因',
              key: 'anomalousCause',
              width: '10%',
              type: FormTypes.input_pop,
              placeholder: '请输入${title}'
            },
            {
              title: '异常图片1',
              key: 'scenePhotos1',
              width: '10%',
              type: FormTypes.upload,
              token: true,
              action: window._CONFIG['domianURL'] + '/sys/common/upload',
              responseName: 'message',
              placeholder: '点击上传'
            },
            {
              title: '异常图片2',
              key: 'scenePhotos2',
              width: '10%',
              type: FormTypes.upload,
              token: true,
              action: window._CONFIG['domianURL'] + '/sys/common/upload',
              responseName: 'message',
              placeholder: '点击上传'
            },
            {
              title: '说明',
              key: 'annotation',
              width: '10%',
              type: FormTypes.input_pop,
              placeholder: '请输入${title}'
            }
          ]
        },
        url: {
          add: '/cable/plan4/consolidationCompleted',
          getPlan4ReceivingStorageList: '/cable/plan4/getPlan4ReceivingStorageList',
          getPlan4DeliverStorage: '/cable/plan4/getPlan4DeliverStorage'
        }
      }
    },
    methods: {
      /**
       * 完单类型 change 事件
       * 2020/8/27 bai
       */
      switchOperatorSchema(type) {
        if (type == 0) {    // 通过改变完单类型来切换不同的 table 信息
          this.tabTitle = '出库完单信息'
          this.defaultTable = this.table1
        } else {
          this.tabTitle = '入库完单信息'
          this.defaultTable = this.table2
        }
      },
      /**
       * 自家仓库
       */
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
      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'defaultTable') // 默认 table
          //getRefPromise(this, 'editableTable1'), // 出库时 table 格式
          //getRefPromise(this, 'editableTable2') // 入库时 table 格式
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
      completePlanModelShow(record) {
        console.log('进入了合并完单页面>>>', record)
        this.activeKey = '1'
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.defaultTable = this.table2 //默认加载第二个 table2
        // 加载子表数据
        if (record) {
          this.plan4Ids = { ids: record.toString() }
          this.requestTableData(this.url.getPlan4ReceivingStorageList, this.plan4Ids, this.table1)
          this.requestTableData(this.url.getPlan4DeliverStorage, this.plan4Ids, this.table2)
        }
      },
      close() {
        this.storageLocations = []
        this.vehicles = {}
        this.visible = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
          /*editableTables[1].initialize()*/
        })
        this.$emit('close')
      },
      /** 查询某个tab的数据 */
      requestTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          if (res.success) {
            this.visible = true
            tab.dataSource = res.result || []
            if (this.url.getPlan3DeliverStorage == url) {
              this.defaultTable.dataSource = tab.dataSource // 刚开始查询给默认 入库 的 table 数据库进行赋值操作
            }
          } else {
            this.visible = false
            if (this.url.getPlan4ReceivingStorageList == url) {
              this.$message.warning(res.message)
            }
            return
          }
        }).finally(() => {
          tab.loading = false
          this.form.setFieldsValue({operatorSchema:'1'})
        })
      },
      handleOk() {
        this.validateFields()
      },
      handleCancel() {
        this.close()
      },
      /** ATab 选项卡切换事件 */
      /*handleChangeTabs(key) {
        /!*if (key == '1') {
          this.form.setFieldsValue({operatorSchema: '0'}) // 通过切换 tab 选项卡的同时切换完单类型 与之绑定
        } else {
          this.form.setFieldsValue({operatorSchema: '1'}) // 通过切换 tab 选项卡的同时切换完单类型 与之绑定
        }*!/
        getRefPromise(this, `editableTable${key}`).then(editableTable => {
          editableTable.resetScrollTop()
        })
      },*/
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
        orderMain.orderDate = orderMain.orderDate ? orderMain.orderDate.format('YYYY-MM-DD HH:mm:ss') : null
        return {
          ...orderMain, // 展开
          completeOrderList: allValues.tablesValue[0].values // 传递到后台的批量填写的表单集合数据
          //jeecgOrderTicketList: allValues.tablesValue[1].values
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        formData['plan4Ids'] = this.plan4Ids.ids
        console.log('发起合并完单的操作方法, 请求参数为>>>>>>', formData)
        if (this.url.add == null) {
          this.$message.error('请设置url.add属性')
          return
        }
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
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