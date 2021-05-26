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

<!--        <a-form-item label="仓库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input-number v-decorator="['type']" placeholder="请输入仓库类型" style="width: 100%"/>-->
<!--        </a-form-item>-->
        <a-form-item label="仓库类型":labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-select v-decorator="['type', validatorRules.type]" placeholder="请选择仓库类型">
            <a-select-option :value="1">自家仓库</a-select-option>
            <a-select-option :value="2">电力公司仓库</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入仓库名称"></a-input>
        </a-form-item>
        <a-form-item label="仓库地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['address', validatorRules.address]" placeholder="请输入仓库地址"></a-input>
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
    name: "WarehouseModal",
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
          type: { rules: [{ required: true, message: '请选择仓库类型' }] },
          name: { rules: [{ required: true, message: '请输入仓库名称' }] },
          address: { },
        },
        url: {
          add: "/cable/warehouse/add",
          edit: "/cable/warehouse/edit",
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
          this.form.setFieldsValue(pick(this.model,'type','name','address','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'type','name','address','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },

      
    }
  }
</script>