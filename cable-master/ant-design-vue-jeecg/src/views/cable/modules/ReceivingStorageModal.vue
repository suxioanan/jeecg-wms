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

        <a-form-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['projectNo']" placeholder="请输入项目编号"></a-input>
        </a-form-item>
        <a-form-item label="处置类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['type']" placeholder="请输入处置类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="物料id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['materialId']" placeholder="请输入物料id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="资产描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['assetText']" placeholder="请输入资产描述"></a-input>
        </a-form-item>
        <a-form-item label="出库数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['receivingNum']" placeholder="请输入出库数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="审核状态(0未审核 1已审)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['state']" placeholder="请输入审核状态(0未审核 1已审)" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sceneSituation']" placeholder="请输入情况" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="现场异常照片路径(路径用逗号隔开)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sceneAbnormalPhotos']" placeholder="请输入现场异常照片路径(路径用逗号隔开)"></a-input>
        </a-form-item>
        <a-form-item label="回单照片路径(路径用逗号隔开)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['receiptPhotos']" placeholder="请输入回单照片路径(路径用逗号隔开)"></a-input>
        </a-form-item>
        <a-form-item label="现场情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['situationText']" placeholder="请输入现场情况"></a-input>
        </a-form-item>
        <a-form-item label="出库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择出库日期" v-decorator="['receivingTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="情况说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sceneKn']" placeholder="请输入情况说明"></a-input>
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
    name: "ReceivingStorageModal",
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
          add: "/cable/receivingStorage/add",
          edit: "/cable/receivingStorage/edit",
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
          this.form.setFieldsValue(pick(this.model,'projectNo','type','materialId','assetText','receivingNum','state','sceneSituation','sceneAbnormalPhotos','receiptPhotos','situationText','receivingTime','sceneKn','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'projectNo','type','materialId','assetText','receivingNum','state','sceneSituation','sceneAbnormalPhotos','receiptPhotos','situationText','receivingTime','sceneKn','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },

      
    }
  }
</script>