<template>
  <a-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 200px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="padding-left: 40px;margin-top: 15px">

        <a-form-item label="任务地址" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 500px">
          <a-input v-decorator="['address']" placeholder="请输入任务地址"></a-input>
        </a-form-item>

        <a-form-item label="联系人" v-if="phones" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-input v-decorator="['linkman']" placeholder="请输入联系人"></a-input>
        </a-form-item>

        <a-form-item label="电话" v-if="phones" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-input v-decorator="['phone']" placeholder="请输入电话"></a-input>
        </a-form-item>

        <a-form-item label="联系人" v-if="phon" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 500px;margin-left: 30px">
          <a-input v-decorator="['linkman']" placeholder="请输入联系人"></a-input>
        </a-form-item>

        <!--<a-form-item label="车辆" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-select
            mode="multiple"
            placeholder="请选择车辆"
            optionFilterProp="children"
            v-decorator="['license',validatorRules.license]"
            :getPopupContainer="(target) => target.parentNode">
            <a-select-option v-for="(role,roleindex) in vehicles" :key="roleindex.toString()" :value="role.license">
              {{ role.license }}
            </a-select-option>
          </a-select>
        </a-form-item>-->

        <!--<a-form-item label="员工" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-select
            mode="multiple"
            placeholder="请选择员工"
            optionFilterProp="children"
            v-decorator="['realname',validatorRules.realname]"
            :getPopupContainer="(target) => target.parentNode">
            <a-select-option v-for="(role,roleindex) in users" :key="roleindex.toString()" :value="role.id">
              {{ role.realname }}
            </a-select-option>
          </a-select>
        </a-form-item>-->

        <!--      任务地址、联系人、联系电话、派单车辆、派单人员    -->
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'


  export default {
    name: "PlanTheSameDayDeModal",
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:700,
        visible: false,
        phones:true,
        phon:false,
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
        //车辆集合
        vehicles: {},
        //人员集合
        users: {},
        validatorRules: {

        },
        url: {
          edit: "/cable/sendOrders/edit",
        },
      }
    },
    created () {
        this.vehiclesList()
        this.usersList()
    },
    methods: {
        //TODO 查询人员集合
        usersList() {
            this.users = ''
            getAction('/cable/plan2/queryUserList').then((res) => {
                if (res.success) {
                    this.users = res.result
                    console.log(this.users)
                }
            })
        },
        //TODO 查询车辆集合
        vehiclesList() {
            this.vehicles = ''
            getAction('/cable/vehicle/selectVehicleList').then((res) => {
                if (res.success) {
                    this.vehicles = res.result
                    console.log(this.vehicles)
                }
            })
        },
      theSames(record){
        this.visible = true;
        this.edit(record);
        // if(record.planType=='4'){
        //   this.phones =false;
        //   this.phon = true;
        // }
      },
      edit (record) {
        console.log("修改信息页面：》》》：",record)
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'linkman','phone','address'))
        })
      },
      close () {
        this.visible = false;
        this.phones = true;
        this.phon = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = 'put';
            httpurl+=this.url.edit;
            let formData = Object.assign(this.model, values);
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
    }
  }
</script>