<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">

      <a-spin :spinning="confirmLoading">
        <a-form :form="form" style="padding-left: 40px;margin-top: 15px">

          <a-form-item label="转移数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number v-decorator="['amount', validatorRules.amount]" style="width: 475px"
                            placeholder="请输入转移数量"></a-input-number>
          </a-form-item>

          <a-form-item label="存放仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['warehouseId', validatorRules.warehouseId]" @change="warehouses"
                      placeholder="请选择存放仓库">
              <template v-for="(warehouse,index) in warehouseLists">
                <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>

          <a-form-item label="存放库位" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['storageLocationRId', validatorRules.storageLocationRId]" placeholder="请选择存放库位">
              <template v-for="(storageLocation,index) in storageLocations">
                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}
                </a-select-option>
              </template>
            </a-select>
          </a-form-item>

          <!--<a-form-item label="新旧/物料" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['oldAndNew',validatorRules.oldAndNew]" placeholder="请选择新旧物料"
                      @change="oldAndNewSelect" style="">
              <a-select-option value="1">新物料</a-select-option>
              <a-select-option value="2">旧物料</a-select-option>
            </a-select>
          </a-form-item>

          <template v-if="oldAndNewflag">
            <a-form-item label="工程账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectNo', validatorRules.projectNo]" placeholder="请输入工程账号">
              </a-input>
            </a-form-item>

            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectName', validatorRules.projectName]" placeholder="请输入工程名称">
              </a-input>
            </a-form-item>

            <a-form-item label="物料代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['serial', validatorRules.serial]" placeholder="请输入物料代码">
              </a-input>
            </a-form-item>

            <a-form-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['materialName', validatorRules.materialName]" placeholder="请输入物料名称">
              </a-input>
            </a-form-item>
          </template>

          <template v-if="!oldAndNewflag">
            <a-form-item label="资产编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['assetNo', validatorRules.assetNo]" placeholder="请输入资产编号">
              </a-input>
            </a-form-item>
          </template>-->

        </a-form>
      </a-spin>

    </a-spin>
  </j-modal>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JEllipsis from '@/components/jeecg/JEllipsis'


  export default {
    name: 'MaterialStorageLocationModal',
    components: {
      JEllipsis
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
        visible: false,
        //新旧物料选择后的展示效果
        oldAndNewflag: true,
        model: {},
        confirmLoading: false,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        validatorRules: {
          projectNo: {rules: [{required: true, message: '请输入工程账号!'}]},
          projectName: {rules: [{required: true, message: '请输入工程名称!'}]},
          serial: {rules: [{required: true, message: '请输入物料代码!'}]},
          materialName: {rules: [{required: true, message: '请输入物料名称!'}]},
          oldAndNew: {rules: [{required: true, message: '请选择新旧物料!'}]},
          amount: {rules: [{required: true, message: '请输入转移数量!'}]},
          warehouseId: {rules: [{required: true, message: '请选择存放仓库!'}]},
          storageLocationRId: {rules: [{required: true, message: '请选择接收库位!'}]},
          assetNo: {rules: [{required: true, message: '请输入资产编号!'}]}
           // pattern: /^[0-9]?$/,
        },
        url: {
          edit: '/cable/storageLocation/MaterialStorageLocationEdit'
        },
        storageLocationId: '',
        warehouseId: '',
        storageLocations: [],
        materials: [],
        materialUnits: {},
        warehouseLists: {}
      }
    },
    created() {
      this.getMaterialUnitList()
      this.getWarehouseList()
    },
    methods: {
      //TODO 新旧物料选择
      oldAndNewSelect(val) {
        if (val == 1) this.oldAndNewflag = true
        else this.oldAndNewflag = false
        // this.oldAndNewflag = !this.oldAndNewflag;
        console.log("选择新旧物料：", val,this.oldAndNewflag)
      },
      warehouses(val) {
        //TODO 清空表单操作
        this.form.setFieldsValue({storageLocationRId:undefined})
        this.storageLocations.length = 0
        this.storageLocationList(val)
      },
      getWarehouseList() {
        getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
          }
        })
      },
      getMaterialUnitList() {
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.materialUnits = res.result
          }
        })
      },
      yiDon(val, kid) {
        this.visible = true
        this.model = val
        this.model.storageLocationCId = kid
        this.storageLocationList()
      },
      storageLocationList(val) {
        this.storageLocations.length = 0
        getAction('/cable/storageLocation/list2', {
          id: this.model.storageLocationCId,
          warehouseId: val
        }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log('加载库位')
            console.log(this.storageLocations)
          }
        })
      },
      materialsList(val) {
        this.materials.length = 0
        getAction('/cable/inventory/insuranceLists', {storageLocationId: val}).then((res) => {
          if (res.success) {
            this.materials = res.result
            console.log('加载物料')
            console.log(this.materials)
          }
        })
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = 'post'
            httpurl += this.url.edit
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
              that.close()
            })
          }

        })
      },
      close() {
        this.form = this.$form.createForm(this)
        this.$emit('close')
        this.visible = false
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>