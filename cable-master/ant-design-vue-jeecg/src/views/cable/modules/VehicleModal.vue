<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 70px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <!--        <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
        <!--          <a-input-number v-decorator="['type']" placeholder="请输入类型" style="width: 100%"/>-->
        <!--        </a-form-item>-->
        <a-form-item label="车辆类型" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px;display: inline-block;margin-bottom: -1px">
          <a-select v-decorator="['type', validatorRules.type]" @change="types" placeholder="请选择车辆类型">
            <template v-for="(types,index) in vehicleTypes">
              <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="载重" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px;margin-bottom: -1px">
          <!--          <a-input-number v-decorator="['carryingCapacity']" placeholder="请输入载重" style="width: 100%"/>-->
          <a-input id="zz" v-decorator="['carryingCapacity',  validatorRules.carryingCapacity]" :readOnly="true"></a-input>
        </a-form-item>
        <a-form-item label="车牌号码" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px;margin-bottom: -1px">
          <a-input v-decorator="['license', validatorRules.license]" placeholder="请输入车牌号码"></a-input>
        </a-form-item>
        <a-form-item label="发动机号" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px;margin-bottom: -1px">
          <a-input v-decorator="['engineNumber', validatorRules.engineNumber]" placeholder="请输入发动机号"></a-input>
        </a-form-item>
        <!--        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px">-->
        <!--        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px">-->
        <!--          <a-input v-decorator="['text', validatorRules.text]" placeholder="请输入备注" style="display: inline-block;height: 50px"></a-input>-->
        <!--        </a-form-item>-->
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" style="margin-bottom: -2px;margin-top: -1px">
          <textarea placeholder="请输入备注" v-decorator="[ 'text', validatorRules.text]" class="textar" ></textarea>
        </a-form-item>
        <!--        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
        <!--          <a-input-number v-decorator="['state']" placeholder="请输入状态" style="width: 100%"/>-->
        <!--        </a-form-item>-->
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 450px;margin-bottom: -1px">
          <a-select v-decorator="['state', validatorRules.state]" placeholder="请选择状态">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">维修</a-select-option>
          </a-select>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {duplicateCheck} from '@/api/api'
  import {httpAction, getAction} from '@/api/manage'


  export default {
    name: "VehicleModal",
    components: {
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:500,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          type: { rules: [{ required: true, message: '请选择车辆类型' }] },
          carryingCapacity: { initialValue:'' },
          license: {
            rules: [{ required: true, message: '请输入车牌号码' },{
              validator: this.licenseJudge
            }]
          },
          engineNumber: { },
          state: { rules: [{ required: true, message: '请选择状态' }],initialValue: 0 },
        },
        url: {
          add: "/cable/vehicle/add",
          edit: "/cable/vehicle/edit",
        },
        vehicleTypes:{},
      }
    },
    created () {
      this.vehicleType();
    },
    methods: {
      licenseJudge(rule, value, callback){
        if (!value) {
          callback()
        } else {
          var params = {
            tableName: 'vehicle',
            fieldName: 'license',
            fieldVal: value,
            dataId: this.model.id
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback()
            } else {
              callback("该车牌号已存在!")
            }
          })

        }
      },
      vehicleType() {
        console.log("vehicleType")
        getAction("/sys/dictItem/selectVehicleType").then((res) => {
          if (res.success) {
            this.vehicleTypes = res.result;
          }
        })
      },
      types(value){
        var that = this;
        console.log(value)
        console.log('value')
        for(var i =0;i<that.vehicleTypes.length;i++){
          if(value==that.vehicleTypes[i].itemValue){
            var vaue = that.vehicleTypes[i].itemAttachValue;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick({carryingCapacity:vaue},'carryingCapacity'));
            });
          }
        }
      },
      add () {
        this.edit({});
      },
      edit (record) {
        if(record.id!=null){
          record.type = record.type+"";
        }
        this.vehicleType();
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'type','carryingCapacity','license','engineNumber','text','state','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'type','carryingCapacity','license','engineNumber','text','state','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },


    }
  }
</script>
<style>
  .textar{
    width: 300px;
    height: 150px;
    display: inline-block;
    margin-top: 15px;
    resize:none;
  }
</style>