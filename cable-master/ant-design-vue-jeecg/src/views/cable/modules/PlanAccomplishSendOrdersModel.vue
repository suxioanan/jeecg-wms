<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 50px">
    <a-spin :spinning="confirmLoading" style="margin-top: 10px">
      <a-form :form="form" style="margin-left: 25px">

        <a-form-item :label="this.dateText" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 340px;margin-right: -20px;margin-right: -50px">
          <j-date style="margin-left: 8px;width: 200px" v-decorator="['handoverDate',validatorRules.handoverDate]"
                  :showTime="true"
                  date-format="YYYY-MM-DD" placeholder="请选择交接时间">
          </j-date>
        </a-form-item>

        <a-form-item label="完单数量" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 340px;margin-right: -90px">
          <a-input style="margin-left: 8px;width: 150px" v-decorator="['accomplishNum',validatorRules.accomplishNum]"
                   placeholder="请输入完单数量"></a-input>
        </a-form-item>

        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 220px;margin-right: -30px">
          <a-select v-decorator="['accomplishNumUnit',validatorRules.accomplishNumUnit]" placeholder="请选择单位"
                    style="margin-left: 8px">
            <template v-for="unit in units">
              <a-select-option v-bind:value="unit.itemValue">{{unit.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>

        <a-form-item label="出库重量" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 300px;margin-left: 8px">
          <a-input style="margin-left: 8px" v-decorator="['accomplishWeight',validatorRules.accomplishWeight]"
                   placeholder="请输入出库重量"></a-input>
        </a-form-item>

        <a-form-item label="单位" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 220px;margin-right: -30px;margin-left: 15px">
          <a-select v-decorator="['accomplishWeightUnit',validatorRules.accomplishWeightUnit]" placeholder="请选择单位"
                    style="width: 150px">
            <template v-for="unit in units">
              <a-select-option v-bind:value="unit.itemValue">{{unit.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>

        <a-form-item label="材质" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 230px;margin-left: 25px;margin-right: -40px">
          <a-select v-decorator="['texture',validatorRules.texture]" placeholder="请选择材质"
                    style="width: 148px;margin-left: 8px">
            <a-select-option value="铜">铜</a-select-option>
            <a-select-option value="铝">铝</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="实际回收规格" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 480px;margin-right: -100px;margin-left: -30px">
          <!--          <a-input style="margin-left: 8px;width: 202px" v-decorator="['recyclingSpecifications',validatorRules.recyclingSpecifications]" placeholder="请输入实际回收规格"></a-input>-->
          <a-select v-decorator="['recyclingSpecifications',validatorRules.recyclingSpecifications]"
                    placeholder="请选择实际回收规格" style="margin-left: 8px;width: 202px">
            <template v-for="(types,index) in recyclingSpecifications">
              <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>

        <a-form-item label="回收情况" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 400px;margin-left: -65px">
          <a-input v-decorator="['recyclingSituation']" placeholder="请输入回收情况" style="width: 373px"></a-input>
        </a-form-item>

        <a-form-item label="电力仓库交接单号" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 650px;margin-left: -27px;margin-right: -320px">
          <a-input v-decorator="['receiptNo',validatorRules.receiptNo]" placeholder="电力仓库交接单号"
                   style="width: 190px"></a-input>
        </a-form-item>

        <a-form-item label="是否异常" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 350px;margin-right: -123px">
          <a-select v-decorator="['sceneSituation',validatorRules.sceneSituation]" placeholder="请选择是否异常"
                    style="width: 150px;margin-left: 5px"
                    @change="yichang">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">异常</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="是否完成" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 400px;margin-right: -150px">
          <a-select v-decorator="['whetherComplete',validatorRules.whetherComplete]" placeholder="请选择是否完成"
                    style="width: 145px">
            <template v-for="(types,index) in completeStates">
              <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>


        <a-form-item label="目标仓库" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 370px">
          <a-select v-decorator="['warehouseId',validatorRules.warehouseId]" @change="types" placeholder="请选择目标仓库"
                    style="width: 300px">
            <template v-for="warehouse in warehouseLists">
              <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
            </template>
          </a-select>
        </a-form-item>

        <a-form-item label="库位" v-if="this.operator" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 370px">
          <a-select v-decorator="['storageLocationId',validatorRules.storageLocationId]" id="sl" placeholder="请选择库位"
                    style="width: 293px">
            <template v-for="storageLocation in storageLocations">
              <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}
              </a-select-option>
            </template>
          </a-select>
        </a-form-item>

        <a-form-item label="完单容积" v-if="this.operator" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 430px;margin-left: -13px;margin-right: -35px">
          <a-input v-decorator="['accomplishVolume',validatorRules.accomplishVolume]" style="width: 300px"
                   placeholder="请输入完单容积"></a-input>
        </a-form-item>

        <a-form-item label="回单" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 350px;margin-left: -30px;margin-right: -50px;float: right">
          <j-image-upload class="avatar-uploader" text="点击上传"
                          v-decorator="['receiptPhotos',validatorRules.receiptPhotos]"
                          style="display: inline-block;width: 300px;height: 85px"></j-image-upload>
        </a-form-item>

        <a-form-item label="施工队" v-if="plan2" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 450px;margin-left: -16px;margin-right: -30px">
          <a-input v-decorator="['constructionTeam']" placeholder="请输入施工队"></a-input>
        </a-form-item>

        <a-form-item label="联系人" v-if="plan2" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 300px;margin-left: 16px;margin-right: -90px">
          <a-input v-decorator="['linkman']" placeholder="请输入联系人" style="width: 150px"></a-input>
        </a-form-item>

        <a-form-item label="电话" v-if="plan2" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 350px;margin-left: -16px;margin-right: -50px">
          <a-input v-decorator="['phone']" placeholder="请输入电话" style="width: 200px"></a-input>
        </a-form-item>

        <a-form-item label="是否异常" v-if="plan2" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 350px;margin-right: -100px">
          <a-select v-decorator="['sceneSituation',validatorRules.sceneSituation]" placeholder="请选择是否异常"
                    style="width: 150px;margin-left: 5px"
                    @change="yichang">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">异常</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="是否异常" v-if="plan1" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;">
          <a-select v-decorator="['sceneSituation',validatorRules.sceneSituation]" placeholder="请选择是否异常"
                    style="width: 300px" @change="yichang">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">异常</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="负责人" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 300px;margin-right: -90px;margin-left: 15px">
          <a-input style="width: 300px" v-decorator="['linkman', validatorRules.linkman]"
                   placeholder="请输入负责人"></a-input>
        </a-form-item>

        <a-form-item label="未完成说明" v-if="plan4" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     style="display: inline-block;width: 500px;margin-left: -25px">
          <a-input v-decorator="['IncompleteDescription']" style="width: 300px" placeholder="未完成说明"></a-input>
        </a-form-item>

        <template v-if="valsjidojfls==1">
          <a-form-item label="异常原因" :labelCol="labelCol" :wrapperCol="wrapperCol" style="margin-left: -110px">
            <a-input v-decorator="['anomalousCause']" placeholder="请输入异常原因" style="width: 660px"></a-input>
          </a-form-item>

          <a-form-item label="异常照片1" :labelCol="labelCol" :wrapperCol="wrapperCol"
                       style="display: inline-block;width: 390px;margin-left: 100px;margin-right: -100px">
            <j-image-upload class="avatar-uploader" text="点击上传"
                            v-decorator="['scenePhotos1',validatorRules.scenePhotos1]"
                            style="display: inline-block;width: 300px;height: 85px;margin-left: 10px"></j-image-upload>
          </a-form-item>

          <a-form-item label="异常照片2" :labelCol="labelCol" :wrapperCol="wrapperCol"
                       style="display: inline-block;width: 350px">
            <j-image-upload class="avatar-uploader" text="点击上传" v-decorator="['scenePhotos2']"
                            style="display: inline-block;width: 300px;height: 70px;margin-left: 5px"></j-image-upload>
          </a-form-item>
        </template>


        <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol" style="margin-left: -110px">
          <a-input v-decorator="['annotation']" placeholder="请输入说明" style="width: 665px"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>

  </a-modal>
</template>

<script>

  import { httpAction, getAction, putAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JImageUpload from '../../../components/jeecg/JImageUpload'

  export default {
    name: 'PlanAccomplishSendOrdersModel',
    components: {
      JDate,
      JImageUpload
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '完单操作',
        width: 870,
        visible: false,
        plan1: false,
        plan2: false,
        plan4: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {
          handoverDate: { rules: [{ required: true, message: '请选择交接日期' }] },
          accomplishNum: { rules: [{ required: true, message: '请输入完单' }] },
          accomplishNumUnit: { rules: [{ required: true, message: '请选择单位' }] },
          recyclingSpecifications: { rules: [{ required: true, message: '请选择实际回收规格' }] },
          texture: { rules: [{ required: true, message: '请选择材质' }] },
          whetherComplete: { rules: [{ required: true, message: '请选择是否完成' }] },
          receiptNo: { rules: [{ required: true, message: '请输入入库交接单号' }] },
          warehouseId: { rules: [{ required: true, message: '请选择目标仓库' }] },
          storageLocationId: { rules: [{ required: true, message: '请选择库位' }] },
          accomplishVolume: { rules: [{ required: true, message: '请输入完单容积' }] },
          receiptPhotos: { rules: [{ required: true, message: '请上传回单' }] },
          sceneSituation: { rules: [{ required: true, message: '请选择是否异常' }] },
          anomalousCause: { rules: [{ required: true, message: '请输入异常原因' }] },
          scenePhotos1: { rules: [{ required: true, message: '请上传异常照片' }] },
          linkman: { rules: [{ required: true, message: '请输入负责人' }] }
        },
        url: {
          edit: '/cable/plan2/planedit'
        },
        // 单位字典数据
        units: {},
        storageLocations: {},
        warehouseLists: {},
        vale: {},
        planTy: '',
        fileList: [],
        valsjidojfls: 0,
        // 操作模式[1入库/0出库]
        operator: '',
        value: '',
        // 完成状态字典数据
        completeStates: {},
        // 实际回收规格字典数据集
        recyclingSpecifications: {},
        /**
         * 入库或出库日期文本
         */
        dateText: ''
      }
    },
    created() {
      this.completeStateList()
      this.getrecyclingSpecifications()
    },
    methods: {
      getrecyclingSpecifications() {
        getAction('/sys/dictItem/getrecyclingSpecifications').then((res) => {
          if (res.success) {
            this.recyclingSpecifications = res.result
          }
        })
      },
      completeStateList() {
        getAction('/sys/dictItem/selectCompleteState').then((res) => {
          if (res.success) {
            this.completeStates = res.result
          }
        })
      },
      yichang(val) {
        this.valsjidojfls = val
      },
      warehouseList() {
        this.warehouseLists = ''
        this.storageLocations = ''
        getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
          }
        })
      },
      types(val) {
        this.storageLocations = []
        this.form.setFieldsValue(pick({ storageLocationId: undefined }, 'storageLocationId'))
        let va = val
        getAction('/cable/storageLocation/list', { warehouseId: va }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log(this.storageLocations)
          }
        })
      },
      dkwdcz(val, planTy, value) {
        console.log("进入dkwdcz 方法 》》：",val)
        console.log('val', val)
        console.log('planTy', planTy)
        console.log('value', value)
        if (value != null) {
          this.value = value
        }
        this.vale = val
        this.model = val
        if (planTy == 1) {
          this.plan1 = true
        } else if (planTy == 4) {
          this.plan4 = true
        } else {
          this.plan2 = true
        }
        if (val.operatorSchema == 1) {
          // 入库
          this.operator = true
          this.dateText = '入库日期'
        } else {
          // 出库
          this.operator = false
          this.dateText = '出库日期'
        }
        this.unlisList()
        this.warehouseList()
        this.visible = true
      },
      unlisList() {
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.units = res.result
          }
        })
      },
      close() {
        this.yichang(0)
        this.model = {}
        this.form = this.$form.createForm(this)
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            this.model.id = this.vale.id
            if (this.value != null) {
              this.model.storageLocationId = this.value
            }
            this.model.operatorSchema = this.vale.operatorSchema

            //提交 资产编号 （存库存表）
            this.model.assetNo = this.vale.assetNo

            let formData = Object.assign(this.model, values)
            console.log("提交表单数据：formData:>>>",formData)
            putAction('/cable/sendOrders/planedit', formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        accomplishWeight
        this.form.setFieldsValue(pick(row, 'handoverDate', 'accomplishNum', 'accomplishNumUnit', 'accomplishWeight', 'accomplishWeightUnit', 'recyclingSpecifications', 'texture', 'recyclingSituation', 'linkman', 'whetherComplete', 'receiptNo', 'warehouseId', 'storageLocationId', 'receiptPhotos', 'constructionTeam', 'phone', 'sceneSituation', 'anomalousCause', 'scenePhotos1', 'scenePhotos2', 'IncompleteDescription', 'annotation'))
      }
    }
  }
</script>
<style>
  .avatar-uploader > .ant-upload {
    width: 104px;
    height: 104px;
  }

  .ant-upload-select-picture-card i {
    font-size: 49px;
    color: #999;
  }
</style>