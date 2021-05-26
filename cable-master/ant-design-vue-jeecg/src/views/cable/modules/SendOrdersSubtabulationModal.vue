<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="派单表id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sendOrdersId']" placeholder="请输入派单表id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="0.车、1.人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['distributionType']" placeholder="请输入0.车、1.人" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="车辆号码/员工id（根据distribution_type变化）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['typeId']" placeholder="请输入车辆号码/员工id（根据distribution_type变化）"></a-input>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['createBy']" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['updateBy']" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="backup1" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['backup1']" placeholder="请输入backup1"></a-input>
        </a-form-item>
        <a-form-item label="backup2" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['backup2']" placeholder="请输入backup2"></a-input>
        </a-form-item>
        <a-form-item label="backup3" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['backup3']" placeholder="请输入backup3"></a-input>
        </a-form-item>
        <a-form-item label="backup4" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['backup4']" placeholder="请输入backup4"></a-input>
        </a-form-item>
        <a-form-item label="backup5" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['backup5']" placeholder="请输入backup5"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  


  export default {
    name: "SendOrdersSubtabulationModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
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
        },
        url: {
          add: "/cable/sendOrdersSubtabulation/add",
          edit: "/cable/sendOrdersSubtabulation/edit",
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
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sendOrdersId','distributionType','typeId','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'sendOrdersId','distributionType','typeId','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },

      
    }
  }
</script>