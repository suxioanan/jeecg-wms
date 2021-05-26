<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="margin-top: 15px">

        <a-form-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['warehouseId',validatorRules.warehouseId]" placeholder="请选择仓库名称">
            <template v-for="warehouse in warehouseLists">
              <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="库位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['storageLocationName',validatorRules.storageLocationName]" placeholder="请输入库位名称"></a-input>
        </a-form-item>
        <a-form-item label="库位容积" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['storageLocationVolume',validatorRules.storageLocationVolume]" placeholder="请输入库位容积" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'


  export default {
    name: "StorageLocationAddModal",
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        //保存自家仓库
        warehouseLists:{},
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
          warehouseId: { rules: [{ required: true, message: '请选择仓库！' }] },
          storageLocationName: { rules: [{ required: true, message: '请输入库位名称！' }] },
          storageLocationVolume: { rules: [{ required: true, message: '请输入库位容积！' }] },
        },
        url: {
          add: "/cable/storageLocation/add",
          edit: "/cable/storageLocation/edit",
          warehouseOneselfList: "/cable/warehouse/warehouseOneselfList",
        }
      }
    },
    created () {
    },
    methods: {
      band(val){
        console.log(val);
        this.validatorRules.warehouseId.initialValue = val;
      },
      addStorageLocation(val){
        console.log('页面打开');
        console.log(val);
        this.visible = true;
        this.validatorRules.storageLocationName.initialValue = '';
        this.validatorRules.storageLocationVolume.initialValue = '';
        this.warehouseList();
        this.band(val);
      },
      warehouseList(){
        console.log('warehouseList');
        this.warehouseLists = '';
        getAction(this.url.warehouseOneselfList).then((res) => {
          if (res.success) {
            this.warehouseLists = res.result;
          }
        })
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.warehouseList();
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'warehouseId','storageLocationName','storageLocationVolume','theCurrentVolume','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
        })
      },
      close () {
        this.form = this.$form.createForm(this);
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
            this.model.theCurrentVolume = 0;
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
        this.form.setFieldsValue(pick(row,'warehouseId','storageLocationName','storageLocationVolume','theCurrentVolume','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },


    }
  }
</script>