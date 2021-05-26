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
      <a-form :form="form">

        <a-form-item label="车牌号码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="cla">
          <j-search-select-tag placeholder="输入车牌号码" v-decorator="[ 'vehicleId',validatorRules.vehicleId]" dict="vehicle,license,id" :async="true" >
          </j-search-select-tag>
        </a-form-item>

        <a-form-item label="保险公司" :labelCol="labelCol" :wrapperCol="wrapperCol" class="cla">
          <a-input v-decorator="['insuraName',validatorRules.insuraName]" placeholder="请输入保险公司"></a-input>
        </a-form-item>
        <a-form-item label="商业险保单号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="cla">
          <a-input v-decorator="['insurancePolicy',validatorRules.insurancePolicy]" placeholder="请输入商业险保单号"></a-input>
        </a-form-item>
        <a-form-item label="商业险日期" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 610px;">
          <j-date style="width: 20%" v-decorator="['insuranceDateBegin',validatorRules.insuranceDateBegin]" :showTime="true" date-format="YYYY-MM-DD" placeholder="请选择开始时间">
          </j-date>
          <span style="width: 10px">—</span>
          <j-date style="width: 20%" v-decorator="['insuranceDateEnd',validatorRules.insuranceDateEnd]" :showTime="true" date-format="YYYY-MM-DD" placeholder="请选择结束时间">
          </j-date>
        </a-form-item>
        <a-form-item label="交强险保单号" class="cla" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['strongPolicy',validatorRules.strongPolicy]" placeholder="请输入交强险保单号"></a-input>
        </a-form-item>
        <a-form-item label="交强险日期" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 610px;">
          <j-date style="width: 20%" v-decorator="['strongDateBegin',validatorRules.strongDateBegin]" :showTime="true" date-format="YYYY-MM-DD" placeholder="请选择开始时间">
          </j-date>
          <span style="width: 10px">—</span>
          <j-date style="width: 20%" v-decorator="['strongDateEnd',validatorRules.strongDateEnd]" :showTime="true" date-format="YYYY-MM-DD" placeholder="请选择结束时间">
          </j-date>
        </a-form-item>
      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: "InsuranceModal",
    components: {
      JDate,
      JSearchSelectTag
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:600,
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
          vehicleId: { rules: [{ required: true, message: '请输入车牌号码' }] },
          insuraName: { },
          insurancePolicy: { },
          insuranceDateBegin: { },
          insuranceDateEnd: { },
          strongPolicy: { },
          strongDateBegin: {  },
          strongDateEnd: { },
        },
        url: {
          add: "/cable/insurance/add",
          edit: "/cable/insurance/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        if(record.id!=null){
          this.validatorRules.insuranceDateBegin.initialValue = record.insuranceDateBegin;
          this.validatorRules.insuranceDateEnd.initialValue = record.insuranceDateEnd;
          this.validatorRules.strongDateBegin.initialValue = record.strongDateBegin;
          this.validatorRules.strongDateEnd.initialValue = record.strongDateEnd;
        }else {
          this.validatorRules.insuranceDateBegin.initialValue = '';
          this.validatorRules.insuranceDateEnd.initialValue = '';
          this.validatorRules.strongDateBegin.initialValue = '';
          this.validatorRules.strongDateEnd.initialValue = '';
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'license','vehicleId','insuraName','insurancePolicy','strongPolicy','insuranceDateBegin','insuranceDateEnd','strongDateBegin','strongDateEnd','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
            formData.license = formData.vehicleId;
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
        this.form.setFieldsValue(pick(row,'license','vehicleId','insuraName','insurancePolicy','strongPolicy','insuranceDateBegin','insuranceDateEnd','strongDateBegin','strongDateEnd','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },
    }
  }
</script>
<style>
  .cla{
    width: 605px;
    margin-left: 2px;
  }
</style>