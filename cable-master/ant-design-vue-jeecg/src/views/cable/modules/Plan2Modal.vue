<template>
  <j-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="margin-top: 20px;margin-left: 25px">

        <a-form-item label="新资产编号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['assetNo', validatorRules.assetNo]" placeholder="请输入新资产编号"  class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="工程项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d">
          <a-input v-decorator="['projectNo', validatorRules.projectNo]" placeholder="请输入工程项目编号"  class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="站点" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['site']" placeholder="请输入站点" ></a-input>
        </a-form-item>
        <a-form-item label="设备名" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['equipmentName']" placeholder="请输入设备名" ></a-input>
        </a-form-item>
        <a-form-item label="退役设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['retiredAssetNo']" placeholder="请输入退役设备编号" class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="容量" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['capacity']" placeholder="请输入容量" ></a-input>
        </a-form-item>
        <a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['model']" placeholder="请输入型号" ></a-input>
        </a-form-item>
        <a-form-item label="ERP现资产状态" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d">
          <a-input v-decorator="['assetStatus']" placeholder="请输入ERP现资产状态"  class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="退役时间" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <j-date placeholder="请选择退役时间" v-decorator="['retiredDate']" :trigger-change="true"  class="inupt_b_a"/>
        </a-form-item>
        <a-form-item label="是否有入库单" :labelCol="labelCol" :wrapperCol="wrapperCol"  class="inupt_c">
          <a-input v-decorator="['receiptIs']" placeholder="请输入是否有入库单"  class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="库存地点" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['theLocation']" placeholder="请输入库存地点" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="处置方式" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['disposalWay']" placeholder="请输入处置方式" ></a-input>
        </a-form-item>
        <a-form-item label="项目分类" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['projectType']" placeholder="请输入项目分类"></a-input>
        </a-form-item>
        <a-form-item label="设备主人" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['equipmentOwners']" placeholder="请输入设备主人" ></a-input>
        </a-form-item>
        <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['address']" placeholder="请输入地址" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['note']" placeholder="请输入备注" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="设备号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['equipmentNo']" placeholder="请输入设备号" ></a-input>
        </a-form-item>
        <a-form-item label="退役但未处置" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['disposed']" placeholder="请输入实物已退役但未处置"  class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['receiptNo']" placeholder="请输入入库单号" ></a-input>
        </a-form-item>
        <a-form-item label="物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['backup2']" placeholder="请输入物料描述" ></a-input>
        </a-form-item>
        <a-form-item label="物料代码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['backup3']" placeholder="请输入物料代码" ></a-input>
        </a-form-item>
        <a-form-item label="完成状态" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-select v-decorator="['completeState', validatorRules.completeState]" class="inupt_b_a" style="width: 220px" placeholder="请选择完成状态">
            <template v-for="(types,index) in completeStates">
              <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
            </template>
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
  import {httpAction, getAction} from '@/api/manage'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'


  export default {
    name: "Plan2Modal",
    components: { 
      JDate,
      JSearchSelectTag
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1400,
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
          planType: { rules: [{ required: true, message: '请输入计划类型' }] },
          projectNo: { rules: [{ required: true, message: '请输入工程编号' }] },
          assetNo: { rules: [{ required: false, message: '请输入新资产编号' }] },
          completeState: { rules: [{ required: true, message: '请选择完成状态' }] },
        },
        url: {
          add: "/cable/plan2/add",
          edit: "/cable/plan2/edit",
        },
        completeStates:{},
      }
    },
    created () {
      this.completeState();
    },
    methods: {
      completeState() {
        getAction("/sys/dictItem/selectCompleteState").then((res) => {
          if (res.success) {
            this.completeStates = res.result;
          }
        })
      },
      add () {
        this.edit({});
      },
      edit (record) {
        if(record.id!=null){
          record.completeState = record.completeState+'';
        }else {
          record.completeState = this.completeStates[1].itemValue;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'planType','site','equipmentName','projectNo','retiredAssetNo','capacity','model','assetStatus','retiredDate','receiptIs','theLocation','disposalWay','assetNo','projectType','equipmentOwners','address','note','equipmentNo','disposed','receiptNo','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'planType','site','equipmentName','projectNo','retiredAssetNo','capacity','model','assetStatus','retiredDate','receiptIs','theLocation','disposalWay','assetNo','projectType','equipmentOwners','address','note','equipmentNo','disposed','receiptNo','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
      },

      
    }
  }
</script>
<style>
  .inupt_a{
    display: inline-block;
    width: 330px;
  }
  .inupt_b{
    display: inline-block;
    width: 400px;
    margin-right: -57px;
    margin-left: -13px;
  }
  .inupt_b_a{
    width: 220px;
  }
  .inupt_c{
    display: inline-block;
    width: 460px;
    margin-right: -102px;
    margin-left: -28px;
  }
  .inupt_d{
    display: inline-block;
    width: 500px;
    margin-right: -135px;
    margin-left: -35px;
  }
</style>