<template>
  <j-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="margin-left: 25px;margin-top: 20px">

        <a-form-item label="新品类型" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-select v-decorator="['planType', validatorRules.planType]" class="inupt_b_a" style="width: 220px" placeholder="请选择所属类型">
            <a-select-option value="新品">新品</a-select-option>
            <a-select-option value="抢修">抢修</a-select-option>
            <a-select-option value="临措">临措</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="工程账号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['projectNo', validatorRules.projectNo]" class="inupt_b_a" style="width: 220px" placeholder="请输入工程账号"></a-input>
        </a-form-item>
        <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['engName', validatorRules.engName]" placeholder="请输入工程名称" style="width: 220px"></a-input>
        </a-form-item>
        <a-form-item label="物料代码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c" style="margin-left: -26px">
          <j-search-select-tag placeholder="输入原物料编码" v-decorator="[ 'materialCode',validatorRules.materialCode]" style="width: 220px" class="inupt_b_a" dict="material,serial,serial" :async="true" >
          </j-search-select-tag>
        </a-form-item>
        <a-form-item label="物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['materialDescribe', validatorRules.materialDescribe]" class="inupt_b_a" style="width: 220px" placeholder="请输入物料描述"></a-input>
        </a-form-item>
        <a-form-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-select v-decorator="['measuringUnit', validatorRules.measuringUnit]" class="inupt_b_a" style="width: 220px" @change="types" placeholder="请选择计量单位">
            <template v-for="(materialUnit,index) in materialUnits">
              <a-select-option v-bind:value="materialUnit.itemValue">{{materialUnit.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="采购申请号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['proApplyNo']" placeholder="请输入采购申请号" class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="采购订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
          <a-input v-decorator="['proTheorderNo']" placeholder="请输入采购订单号" class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['supplier']" placeholder="请输入供应商"></a-input>
        </a-form-item>
        <a-form-item label="需求数量" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input-number v-decorator="['num']" placeholder="请输入需求数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="项目经理" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['projectManager']" placeholder="请输入项目经理"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['mPhone']" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="施工单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <a-input v-decorator="['companyName']" placeholder="请输入施工单位名称"class="inupt_b_a"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
          <a-input v-decorator="['cPhone']" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="送货地点" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 300px">
          <a-input v-decorator="['address']" placeholder="请输入送货地点" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="反馈意见" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['feedback']" placeholder="反馈意见" style="width: 550px"></a-input>
        </a-form-item>
        <a-form-item label="具体施工日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <j-date placeholder="请选择具体施工日期" v-decorator="['constructionTime']" class="inupt_b_a" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="开始送货日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <j-date placeholder="请选择开始送货日期" v-decorator="['startTime']" class="inupt_b_a" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="最终到货日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
          <j-date placeholder="请选择最终到货日期" v-decorator="['stopTime']" class="inupt_b_a" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a" style="margin-right: 330px">
          <a-input v-decorator="['note']" placeholder="请输入备注" style="width: 550px"></a-input>
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

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'


  export default {
    name: "Plan3Modal",
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
          planType: { rules: [{ required: true, message: '请选择计划类型' }] },
          projectNo: { rules: [{ required: true, message: '请输入工程账号' }] },
          engName: { rules: [{ required: true, message: '请输入与工程名称' }] },
          materialCode: { rules: [{ required: true, message: '请输入物料代码' }] },
          materialDescribe: { rules: [{ required: true, message: '请输入物料描述' }] },
          measuringUnit: { rules: [{ required: true, message: '请输入计量单位' }] },
          completeState: { rules: [{ required: true, message: '请选择完成状态' }] }
        },
        url: {
          add: "/cable/plan3/add",
          edit: "/cable/plan3/edit",
        },
        completeStates:{},
        materialUnits:{},
      }
    },
    created () {
      this.completeState();
      this.getMaterialUnitList();
    },
    methods: {
      /* 查询字典单位数据集 */
      getMaterialUnitList() {
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.materialUnits = res.result
          }
        })
      },
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
          this.form.setFieldsValue(pick(this.model,'planType','projectNo','engName','proApplyNo','proTheorderNo','lineitemNo','materialCode','materialDescribe','measuringUnit','supplier','num','projectManager','mPhone','companyName','cPhone','address','feedback','constructionTime','startTime','stopTime','note','instructions','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'planType','projectNo','engName','proApplyNo','proTheorderNo','lineitemNo','materialCode','materialDescribe','measuringUnit','supplier','num','projectManager','mPhone','companyName','cPhone','address','feedback','constructionTime','startTime','stopTime','note','instructions','alreadyDeliverStorage','alreadyReceivingStorage','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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