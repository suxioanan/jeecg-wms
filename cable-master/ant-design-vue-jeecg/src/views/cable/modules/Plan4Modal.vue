<template>
  <j-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="margin-top: 20px;margin-left: 25px">

        <a-form-item label="工程账号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['projectNo', validatorRules.projectNo]" class="inupt_b_a" style="width: 220px" placeholder="请输入工程账号"></a-input>
        </a-form-item>
        <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['engName', validatorRules.engName]" class="inupt_b_a" style="width: 220px" placeholder="请输入工程名称"></a-input>
        </a-form-item>
        <a-form-item label="电缆名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['cableName']" placeholder="请输入电缆名称"></a-input>
        </a-form-item>
        <a-form-item label="电压等级" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['voltageGrade']" placeholder="请输入电压等级"></a-input>
        </a-form-item>
        <a-form-item label="电缆截面" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['cableCross']" placeholder="请输入电缆截面"></a-input>
        </a-form-item>
        <a-form-item label="预计抽取长度" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['samplingLength']" placeholder="请输入预计抽取长度" class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="抽取日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <j-date placeholder="请选择抽取日期" v-decorator="['samplingDate']" class="inupt_b_a" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="抽取地点" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['samplingAddres']" placeholder="请输入抽取地点"></a-input>
        </a-form-item>
        <a-form-item label="施工班组" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['construc']" placeholder="请输入施工班组"></a-input>
        </a-form-item>
        <a-form-item label="联系人及电话" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['construcContact']" placeholder="请输入联系人及电话" class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="施工队伍" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['team']" placeholder="请输入施工队伍"></a-input>
        </a-form-item>
        <a-form-item label="联系人及电话" :labelCol="labelCol" :wrapperCol="wrapperCol"  class="inupt_c">
          <a-input v-decorator="['teamContact']" placeholder="请输入联系人及电话" class="inupt_b_a"  ></a-input>
        </a-form-item>
        <a-form-item label="专职" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['full']" placeholder="请输入专职"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a"  style="margin-right: 500px">
          <a-input v-decorator="['note']" placeholder="请输入备注" style="width: 880px"></a-input>
        </a-form-item>
        <a-form-item label="运检反馈" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['feedback']" placeholder="请输入运检反馈" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="完成状态" :labelCol="labelCol" :wrapperCol="wrapperCol"  class="inupt_b">
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

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  


  export default {
    name: "Plan4Modal",
    components: { 
      JDate,
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
          projectNo: { rules: [{ required: true, message: '请输入工程账号' }] },
          engName: { rules: [{ required: true, message: '请输入与工程名称' }] },
          completeState: { rules: [{ required: true, message: '请选择完成状态' }] },
        },
        url: {
          add: "/cable/plan4/add",
          edit: "/cable/plan4/edit",
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
          record.measuringUnit = record.measuringUnit+'';
          record.completeState = record.completeState+'';
        }else {
          record.completeState = this.completeStates[1].itemValue;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'engName','projectNo','cableName','voltageGrade','cableCross','samplingLength','samplingDate','samplingAddres','construc','construcContact','team','teamContact','full','note','feedback','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'engName','projectNo','cableName','voltageGrade','cableCross','samplingLength','samplingDate','samplingAddres','construc','construcContact','team','teamContact','full','note','feedback','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
</style>