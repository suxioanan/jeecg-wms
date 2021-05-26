<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <template v-if="!this.show">
        <a-descriptions title="">
          <a-descriptions-item label="可出库重量">
            {{this.availableWeight}}
          </a-descriptions-item>
        </a-descriptions>
    </template>

    <a-spin :spinning="confirmLoading">
      <a-form
        :form="form"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 12 }">

        <a-row>
          <a-col :span="8">
            <a-form-item :label="this.dateText">
              <j-date v-decorator="['handoverDate',validatorRules.handoverDate]" :showTime="true"
                      date-format="YYYY-MM-DD" placeholder="请选择交接时间">
              </j-date>
            </a-form-item>
          </a-col>

          <template v-if="this.show">
            <a-col :span="8">
              <a-form-item label="入库重量">
                <a-input v-decorator="['accomplishWeight',validatorRules.accomplishWeight]"
                         placeholder="请输入入库重量"></a-input>
              </a-form-item>
            </a-col>
<!--            v-show="this.show"-->
          </template>

          <template v-if="!this.show">
            <a-col :span="8">
              <a-form-item label="实际出库重量">
                <a-input v-decorator="['accomplishWeight',validatorRules.accomplishWeight]"
                         placeholder="请输入出库重量"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :span="8">
            <a-form-item label="完单容积">
              <a-input v-decorator="['accomplishVolume',validatorRules.accomplishVolume]"
                       placeholder="请输入完单容积"></a-input>
            </a-form-item>
          </a-col>

        </a-row>

        <a-row>
          <a-col :span="8">
            <a-form-item label="完单数量">
              <a-input v-decorator="['accomplishNum',validatorRules.accomplishNum]" placeholder="请输入完单数量"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="单位">
              <a-select v-decorator="['accomplishNumUnit',validatorRules.accomplishNumUnit]" placeholder="请选择单位">
                <a-select-option v-for="(unit,index) in unitList" :value="unit.itemValue">{{unit.itemText}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="材质">
              <a-select v-decorator="['texture',validatorRules.texture]" placeholder="请选择材质">
                <a-select-option value="铜">铜</a-select-option>
                <a-select-option value="铝">铝</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="8">
            <a-form-item label="实际回收规格">
              <a-select v-decorator="['recyclingSpecifications',validatorRules.recyclingSpecifications]"
                        placeholder="请选择实际回收规格">
                <a-select-option v-for="(item,index) in recyclingSpecifications" :value="item.itemValue">
                  {{item.itemText}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="this.show">
            <a-col :span="8">
              <a-form-item label="是否异常">
                <a-select v-decorator="['sceneSituation',validatorRules.sceneSituation]" placeholder="请选择是否异常"
                          @change="isException">
                  <a-select-option :value="0">正常</a-select-option>
                  <a-select-option :value="1">异常</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="是否完成">
                <a-select v-decorator="['whetherComplete',validatorRules.whetherComplete]" placeholder="请选择是否完成">
                  <a-select-option v-for="(item,index) in completeStates" :value="item.itemValue">{{item.itemText}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>

          <a-col :span="8">
            <a-form-item label="目标仓库">
              <a-select v-decorator="['warehouseId',validatorRules.warehouseId]" placeholder="请选择目标仓库"
                        @change="changeWarehouse">
                <a-select-option v-for="(item,index) in warehouseLists" :value="item.id">{{item.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="this.show">
            <a-col :span="8">
              <a-form-item label="库位">
                <a-select v-decorator="['storageLocationId',validatorRules.storageLocationId]" placeholder="请选择库位">
                  <a-select-option v-for="(item,index) in storageLocations" :value="item.id">
                    {{item.storageLocationName}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
          <a-col :span="8">
            <a-form-item label="负责人">
              <a-input v-decorator="['linkman', validatorRules.linkman]" placeholder="请输入负责人"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="回收情况">
              <a-input v-decorator="['recyclingSituation']" placeholder="请输入回收情况"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="this.show">
            <a-col :span="12">
              <a-form-item label="未完成说明">
                <a-input v-decorator="['IncompleteDescription']" placeholder="未完成说明"></a-input>
              </a-form-item>
            </a-col>
          </template>
        </a-row>

        <a-row>
          <template v-if="this.show">
            <a-col :span="16">
              <a-form-item label="项目报废物资入库交接单号">
                <a-input v-decorator="['receiptNo',validatorRules.receiptNo]" placeholder="项目报废物资入库交接单号"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :span="12" v-show="!this.show">
            <a-form-item label="电力仓库交接单号">
              <a-input v-decorator="['receiptNo',validatorRules.receiptNo]" placeholder="电力仓库交接单号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="回单">
              <j-image-upload text="点击上传" v-decorator="['receiptPhotos',validatorRules.receiptPhotos]"></j-image-upload>
            </a-form-item>
          </a-col>
        </a-row>

        <template v-if="this.show">
          <a-row>
            <a-form-item label="说明" style="margin-left: -310px">
              <a-input v-decorator="['annotation']" placeholder="请输入说明" style="width: 665px"></a-input>
            </a-form-item>
          </a-row>
        </template>

        <!-- 是否异常[0正常/1异常] -->
        <template v-if="this.isFlag==1">
          <a-row>
            <a-col :span="16">
              <a-form-item label="异常原因">
                <a-input v-decorator="['anomalousCause']" placeholder="请输入异常原因"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8"></a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="异常照片1">
                <j-image-upload text="点击上传" v-decorator="['scenePhotos1',validatorRules.scenePhotos1]"></j-image-upload>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="异常照片2">
                <j-image-upload text="点击上传" v-decorator="['scenePhotos2']"></j-image-upload>
              </a-form-item>
            </a-col>
          </a-row>
        </template>

      </a-form>
    </a-spin>

  </a-modal>
</template>

<script>
  import JDate from '@/components/jeecg/JDate'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import {getAction, putAction} from '../../../api/manage'

  export default {
    name: 'PlanAccomplishSendOrdersModel4',
    components: {JDate, JImageUpload},
    data() {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 1000,
        visible: false,
        confirmLoading: false,
        /**
         * 计划4的入库和出库区分显示
         */
        show: false,
        /**
         * 异常状态[0正常/1异常]
         */
        isFlag: 0,
        /**
         * 要完单的数据集
         */
        model: {},
        /**
         * 表单验证规则
         */
        validatorRules: {
          handoverDate: {rules: [{required: true, message: '请选择交接日期'}]},
          accomplishNum: {rules: [{required: true, message: '请输入完单'}]},
          accomplishNumUnit: {rules: [{required: true, message: '请选择单位'}]},
          texture: {rules: [{required: true, message: '请选择材质'}]},
          recyclingSpecifications: {rules: [{required: true, message: '请选择实际回收规格'}]},
          receiptNo: {rules: [{required: true, message: '请输入电力仓库交接单号'}]},
          sceneSituation: {rules: [{required: true, message: '请选择是否异常'}]},
          whetherComplete: {rules: [{required: true, message: '请选择是否完成'}]},
          warehouseId: {rules: [{required: true, message: '请选择目标仓库'}]},
          storageLocationId: {rules: [{required: true, message: '请选择库位'}]},
          scenePhotos1: {rules: [{required: true, message: '请上传异常照片'}]},
          accomplishVolume: {rules: [{required: true, message: '请输入完单容积'}]},
          linkman: {rules: [{required: true, message: '请输入负责人'}]},
          receiptPhotos: {rules: [{required: true, message: '请上传回单'}]},
          accomplishWeight: {rules: [{required: true, message: '请输入入库重量'}]},
        },
        /**
         * 单位字典数据集
         */
        unitList: {},
        /**
         * 实际回收规格字典数据集
         */
        recyclingSpecifications: {},
        /**
         * 完成状态字典数据集
         */
        completeStates: {},
        /**
         * 全部仓库数据集
         */
        warehouseLists: {},
        /**
         * 全部库位数据集
         */
        storageLocations: {},
        vale: {},
        /**
         * 入库或出库日期文本
         */
        dateText: '',
        /**
         * 可出库重量
         */
        availableWeight: 0
      }
    },
    created() {
      this.getUnitListAll()
      this.getRecyclingSpecifications()
      this.completeStateList()
      this.warehouseList()
    },
    methods: {
      /**
       * 提交事件
       */
      handleOk() {
        console.log("点击了按钮！！！！！！！！！！！！")
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            this.model.id = this.vale.id
            if (this.value != null) {
              this.model.storageLocationId = this.value
            }
            this.model.operatorSchema = this.vale.operatorSchema
            let formData = Object.assign(this.model, values)
            console.log(formData)
            if ((Number(formData.accomplishWeight) > Number(this.availableWeight)) && !this.show) {
              //TODO 实际出库重量不能大于可出库重量
              that.$message.warning("实际出库重量不能大于可出库重量!")
              that.confirmLoading = false
              return
            }
            putAction('/cable/sendOrders/planedit', formData).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
              console.log("res>>>>>:",res)
            }).finally(() => {
              that.confirmLoading = false
              that.handleCancel()
            })
          }
        })
      },
      /**
       * 关闭
       */
      handleCancel() {
        this.availableWeight = ''
        this.isException(0)
        this.modal = {}
        this.form = this.$form.createForm(this)
        this.$emit('close')
        this.visible = false
      },
      /**
       * 组件显示方法
       */
      plan4show(value) {
        this.vale = value
        this.visible = true
        //TODO 可出库重量（作用参考）
        this.availableWeight = (value.availableWeight==null?'暂无可出库重量':value.availableWeight)
        console.log("完单操作表单页面传递数据为>>>>>>>>>>>>>>>>>>>>>>>>>>", value)
        if (value.operatorSchema == 1) {
          // 操作模式为入库
          this.show = true
          this.dateText = '入库日期'
        } else {
          // 操作模式为出库
          this.show = false
          this.dateText = '出库日期'
        }
      },
      /**
       * 查询全部单位数据
       */
      getUnitListAll() {
        console.log("查询所有单位数据 ！")
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.unitList = res.result
          }
        })
      },
      /**
       * 查询全部回收规则数据
       */
      getRecyclingSpecifications() {
        getAction('/sys/dictItem/getrecyclingSpecifications').then((res) => {
          if (res.success) {
            this.recyclingSpecifications = res.result
          }
        })
      },
      /**
       * 是否异常
       * @param status 0正常/1异常
       */
      isException(status) {
        this.isFlag = status
      },
      /**
       * 查询全部完成状态数据
       */
      completeStateList() {
        getAction('/sys/dictItem/selectCompleteState').then((res) => {
          if (res.success) {
            this.completeStates = res.result
          }
        })
      },
      /**
       * 查询全部仓库数据
       */
      warehouseList() {
        getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
          }
        })
      },
      /**
       * 目标仓库 change 事件
       * @param Id 仓库 id
       */
      changeWarehouse(Id) {
        console.log('目标仓库 change 事件', Id)
        // 根据仓库 id 查询对应的库位信息
        getAction('/cable/storageLocation/list', {warehouseId: Id}).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
          }
        })
      }
    }
  }
</script>