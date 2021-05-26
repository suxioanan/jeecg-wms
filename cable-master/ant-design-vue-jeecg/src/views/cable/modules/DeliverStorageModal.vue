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

        <a-form-item label="派单id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sendOrdersId']" placeholder="请输入派单id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="物料id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['materialId']" placeholder="请输入物料id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="仓库id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['warehouseId']" placeholder="请输入仓库id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="库位id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['storageLocationId']" placeholder="请输入库位id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="完单数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['deliverNum']" placeholder="请输入完单数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="单位（字典）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['unit']" placeholder="请输入单位（字典）" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="审核状态(字典0未审核 1已审)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['state']" placeholder="请输入审核状态(字典0未审核 1已审)" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否异常(字典0正常 1异常)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sceneSituation']" placeholder="请输入是否异常(字典0正常 1异常)" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="异常原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['anomalousCause']" placeholder="请输入异常原因"></a-input>
        </a-form-item>
        <a-form-item label="现场照片路径(路径用逗号隔开)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['scenePhotos']" placeholder="请输入现场照片路径(路径用逗号隔开)"></a-input>
        </a-form-item>
        <a-form-item label="回单照片路径(路径用逗号隔开)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['receiptPhotos']" placeholder="请输入回单照片路径(路径用逗号隔开)"></a-input>
        </a-form-item>
        <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择入库日期" v-decorator="['deliverTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['annotation']" placeholder="请输入说明"></a-input>
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
    name: "DeliverStorageModal",
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
          add: "/cable/deliverStorage/add",
          edit: "/cable/deliverStorage/edit",
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
          this.form.setFieldsValue(pick(this.model,'sendOrdersId','materialId','warehouseId','storageLocationId','deliverNum','unit','state','sceneSituation','anomalousCause','scenePhotos','receiptPhotos','deliverTime','annotation','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'sendOrdersId','materialId','warehouseId','storageLocationId','deliverNum','unit','state','sceneSituation','anomalousCause','scenePhotos','receiptPhotos','deliverTime','annotation','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },

      
    }
  }
</script>