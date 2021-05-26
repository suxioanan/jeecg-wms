<template>
  <a-modal
    :title="title"
    :width="650"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 30px">
    <a-form :form="form" style="margin-top: 20px">
      <a-row :gutter="24">
        <a-col :md="24" :sm="24">
      <a-form-item label="计划完成状态" :labelCol="labelCol" :wrapperCol="wrapperCol" >
        <a-select v-decorator="['completeState', validatorRules.completeState]" @change="setStatus" style="width: 50%">
          <a-select-option :value="0">未完成</a-select-option>
          <a-select-option :value="1">已完成</a-select-option>
        </a-select>
      </a-form-item>
        </a-col>

      </a-row>
    </a-form>

  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import { httpAction } from '../../../api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'PlanCompleteStateModal',
      components: {
          pick,
          JDate,
          JEllipsis,
      },
    data() {
      return {
        description: '计划完成状态页面',
        form: this.$form.createForm(this),
        title: '计划完成状态',
        visible: false,
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        validatorRules: {
          completeState: { rules: [{ required: true, message: '请选择计划完成状态' }] }
        },
        model: {},
        planType: '',
        //初始状态
        status: ''
      }
    },
    methods: {
        //TODO 点击状态
        setStatus(val){
            this.status = val;
        },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            let http = ''
            if (this.planType === 1) {
              http = '/cable/plan1/edit'
            }
            if (this.planType === 2) {
              http = '/cable/plan2/edit'
            }
            if (this.planType === 3) {
              http = '/cable/plan3/edit'
            }
            if (this.planType === 4) {
              http = '/cable/plan4/edit'
            }
            this.model.completeState = this.status
            let formData = Object.assign(this.model, values)
            console.log('formData数据为', formData)
            console.log('http 接口为', http)
            httpAction(http, formData, 'put').then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.handleCancel()
            })
          }
        })
      },
      /**
       * 计划完成状态显示
       * @param record 要更新的数据
       * @param type 计划类型区分[1/2/3/4]
       */
      show(record, type) {
        // 计划类型赋值
        this.planType = type
        // 清空表单数据
        this.form.resetFields()
        // 对 model 进行赋值操作
        this.model = Object.assign({}, record)
        // 展示弹出框
        this.visible = true
        this.status = record.completeState
        this.model.completeState = record.completeState_dictText

        // 向表单 form 进行赋值操作
        this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model, 'completeState'))
        })
      },
      handleCancel() {
        this.visible = false
      }
    }
  }
</script>

<style scoped>

  .inupt_b{
    display: inline-block;
    width: 400px;
    margin-right: -57px;
    margin-left: -13px;
  }
</style>