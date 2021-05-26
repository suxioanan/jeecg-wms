<template>
  <j-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="padding-left: 40px;margin-top: 15px">

          <div style="margin-bottom: -15px">
            <a-form-item label="计划类型" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
              <a-select v-decorator="['planType', validatorRules.planType]" placeholder="请选择计划类型" style="width: 220px">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="废旧物料编码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d" style="margin-left: -36px">
              <j-search-select-tag placeholder="输入废旧物料编码" v-decorator="[ 'wasteMaterialCode',validatorRules.wasteMaterialCode]" style="width: 220px" class="inupt_b_a" dict="material,serial,serial" :async="true" >
              </j-search-select-tag>
            </a-form-item>
            <a-form-item label="废旧物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">
              <a-input v-decorator="['wasteMaterialText', validatorRules.wasteMaterialText]" placeholder="请输入废旧物料描述" class="inupt_b_a"></a-input>
            </a-form-item>
            <a-form-item label="废旧物料单位" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">
              <a-select v-decorator="['wasteMaterialUnit', validatorRules.wasteMaterialUnit]" placeholder="请选择废旧物料单位" class="inupt_b_a" style="width: 220px" >
                <template v-for="(materialUnit,index) in materialUnits">
                  <a-select-option v-bind:value="materialUnit.itemValue">{{materialUnit.itemText}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="项目编码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['projectNo', validatorRules.projectNo]" placeholder="请输入项目编码" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b" style="margin-right: 273px">
            <a-input v-decorator="['projectName', validatorRules.projectName]" placeholder="请输入项目名称"class="inupt_b_a" style="width: 550px"></a-input>
          </a-form-item>
          <a-form-item label="资产编码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['assetNo', validatorRules.assetNo]" placeholder="请输入资产编码" class="inupt_b_a"></a-input>
          </a-form-item>
        </div>

        <div style="margin-bottom: -15px">
          <a-form-item label="交接单数量" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">
            <a-input v-decorator="['numReceipts', validatorRules.numReceipts]" placeholder="请输入交接单数量" class="inupt_b_a" style="width: 220px"/>
          </a-form-item>
          <a-form-item label="所属工厂" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['theFactory']" placeholder="请输入所属工厂" ></a-input>
          </a-form-item>
          <a-form-item label="工厂描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['factoryText']" placeholder="请输入工厂描述" ></a-input>
          </a-form-item>
          <a-form-item label="处置方式" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['disposalWay']" placeholder="请输入处置方式" ></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="库存点描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b" style="margin-right: 272px">
            <a-input v-decorator="['theLocationText']" placeholder="请输入库存点描述" class="inupt_b_a" style="width: 550px"></a-input>
          </a-form-item>

          <a-form-item label="原物料编码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c" style="margin-left: -26px">
            <j-search-select-tag placeholder="输入原物料编码" v-decorator="[ 'rawMaterialCode',validatorRules.rawMaterialCode]" style="width: 220px" class="inupt_b_a" dict="material,serial,serial" :async="true" >
            </j-search-select-tag>
          </a-form-item>

          <a-form-item label="原物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c"style="margin-left: -30px">
            <a-input v-decorator="['rawMaterialText', validatorRules.rawMaterialText]" placeholder="请输入原物料描述"  class="inupt_b_a"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="原物料单位" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">
            <a-select v-decorator="['rawMaterialUnit', validatorRules.rawMaterialUnit]" class="inupt_b_a" style="width: 220px" placeholder="请选择原物料单位">
              <template v-for="(materialUnit,index) in materialUnits">
                <a-select-option v-bind:value="materialUnit.itemValue">{{materialUnit.itemText}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>
<!--          <a-form-item label="废旧物料编码" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">-->
<!--            <a-input v-decorator="['wasteMaterialCode', validatorRules.wasteMaterialCode]" placeholder="请输入废旧物料编码" class="inupt_b_a"></a-input>-->
<!--          </a-form-item>-->
          <a-form-item label="交接单号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['itemSlip']" placeholder="请输入项目交接单"></a-input>
          </a-form-item>
          <a-form-item label="交接单类型" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['itemSlipType']" placeholder="请输入项目交接单类型"class="inupt_b_a" ></a-input>
          </a-form-item>
          <a-form-item label="业务类型" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['businessType']" placeholder="请输入业务类型"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="库存地点" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['theLocation']" placeholder="请输入库存地点" ></a-input>
          </a-form-item>
          <a-form-item label="实际入库数量" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['deliverNum']" placeholder="请输入实际入库数量" class="inupt_b_a"/>
          </a-form-item>
          <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <j-date placeholder="请选择入库日期" v-decorator="['deliverTime']" :trigger-change="true"class="inupt_b_a"/>
          </a-form-item>
          <a-form-item label="入库凭证" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['deliverVoucher']" placeholder="请输入入库凭证" ></a-input>
          </a-form-item>
        </div>

        <div style="margin-bottom: -15px">
          <a-form-item label="资产名称" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['assetText']" placeholder="请输入资产名称" ></a-input>
          </a-form-item>
          <a-form-item label="实物ID" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['realId']" placeholder="请输入实物ID" ></a-input>
          </a-form-item>
          <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['equipmentDeveui']" placeholder="请输入设备编号" ></a-input>
          </a-form-item>
          <a-form-item label="技术对象说明" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['objectInstruct']" placeholder="请输入技术对象说明" class="inupt_b_a"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="制造商型号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['manufacturing']" placeholder="请输入制造商型号" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="出厂编号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['factoryNo']" placeholder="请输入出厂编号" ></a-input>
          </a-form-item>
          <a-form-item label="报废原值" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['wasteVal']" placeholder="请输入报废原值" ></a-input>
          </a-form-item>
          <a-form-item label="报废理由" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_a">
            <a-input v-decorator="['wasteReason']" placeholder="请输入报废理由" ></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="交接单创建人" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['deliveryCreateBy']" placeholder="请输入交接单创建人" class="inupt_b_a" ></a-input>
          </a-form-item>
          <a-form-item label="交接单创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d" style="margin-left: -40px">
            <j-date placeholder="请选择交接单创建日期" v-decorator="['deliveryCreateTime']" :trigger-change="true" style="margin-left: 5px"class="inupt_b_a"/>
          </a-form-item>
          <a-form-item label="交接单接收日期" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d" style="margin-left: -35px">
            <j-date placeholder="请选择交接单接收日期" v-decorator="['deliveryReceiveTime']" :trigger-change="true"style="margin-left: 5px" class="inupt_b_a"/>
          </a-form-item>
          <a-form-item label="项目单位联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d" style="margin-left: -35px">
            <a-input v-decorator="['projectContact']" placeholder="请输入项目单位联系人" class="inupt_b_a"style="margin-left: 5px"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="项目单位联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_e">
            <a-input v-decorator="['projectPhone']" placeholder="请输入项目单位联系电话" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="现场联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['theContact']" placeholder="请输入现场联系人" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="现场联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['thePhone']" placeholder="请输入现场联系电话" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="仓库联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['warehouseContact']" placeholder="请输入仓库联系人" class="inupt_b_a"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="仓库联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['warehousePhone']" placeholder="请输入仓库联系电话" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="拆除计划说明" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c" style="margin-right: 230px">
            <a-input v-decorator="['demolitionPlan']" placeholder="请输入拆除计划说明" class="inupt_b_a" style="width: 550px"></a-input>
          </a-form-item>

          <a-form-item label="系统系列号" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['systemNo']" placeholder="请输入系统系列号" class="inupt_b_a"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="入库/冲销备注" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['noteInformation']" placeholder="请输入入库/冲销备注" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="工程详细地址" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c" style="margin-right: 230px">
            <a-input v-decorator="['engineeringAddress']" placeholder="请输入工程详细地址" class="inupt_b_a" style="width: 550px"></a-input>
          </a-form-item>


          <a-form-item label="项目编码备注" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_c">
            <a-input v-decorator="['projectNoRemarks']" placeholder="请输入项目编码备注" class="inupt_b_a"></a-input>
          </a-form-item>
        </div>
        <div style="margin-bottom: -15px">
          <a-form-item label="资产成新率" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-input v-decorator="['rateFormation']" placeholder="请输入资产成新率" class="inupt_b_a"></a-input>
          </a-form-item>
          <a-form-item label="计划报废完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d">
            <j-date placeholder="请选择计划报废完成时间" v-decorator="['plansCompleteTime']" :trigger-change="true" class="inupt_b_a"/>
          </a-form-item>
          <a-form-item label="完成状态" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_b">
            <a-select v-decorator="['completeState', validatorRules.completeState]" placeholder="请选择完成状态" style="width: 218px">
              <template v-for="(types,index) in completeStates">
                <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>
        </div>
      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'


  export default {
    name: "Plan1Modal",
    components: {
      JDate,
      JSearchSelectTag
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1410,
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
          rawMaterialCode: { rules: [{ required: false, message: '请输入原物料编码' }] },
          rawMaterialText: { rules: [{ required: false, message: '请输入原物料描述' }] },
          rawMaterialUnit: { rules: [{ required: false, message: '请选择原物料单位' }] },
          wasteMaterialCode: { rules: [{ required: true, message: '请输入废旧物料编码' }] },
          wasteMaterialText: { rules: [{ required: true, message: '请输入废旧物料描述' }] },
          wasteMaterialUnit: { rules: [{ required: true, message: '请选择废旧物料单位' }] },
          numReceipts: { rules: [{ required: true, message: '请输入交接单数量' }] },
          projectNo: { rules: [{ required: true, message: '请输入项目编码' }] },
          projectName: { rules: [{ required: true, message: '请输入项目名称' }] },
          completeState: { rules: [{ required: true, message: '请选择完成状态' }] },
          assetNo: { rules: [{ required: true, message: '请输入资产编码' }] },
        },
        url: {
          add: "/cable/plan1/add",
          edit: "/cable/plan1/edit",
        },
        //完成状态
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
          record.rawMaterialUnit=record.rawMaterialUnit+'';
          record.wasteMaterialUnit=record.wasteMaterialUnit+'';
          record.completeState = record.completeState+'';
        }else {
          record.completeState = this.completeStates[1].itemValue;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'planType','itemSlipType','itemSlip','businessType','theFactory','factoryText','disposalWay','theLocation','theLocationText','rawMaterialCode','rawMaterialText','rawMaterialUnit','wasteMaterialCode','wasteMaterialText','wasteMaterialUnit','numReceipts','alreadyDeliverStorage','alreadyReceivingStorage','deliverNum','deliverTime','deliverVoucher','projectNo','projectName','assetNo','assetText','realId','equipmentDeveui','objectInstruct','manufacturing','factoryNo','wasteVal','wasteReason','deliveryCreateBy','deliveryCreateTime','deliveryReceiveTime','projectContact','projectPhone','theContact','thePhone','warehouseContact','warehousePhone','demolitionPlan','engineeringAddress','systemNo','noteInformation','projectNoRemarks','rateFormation','plansCompleteTime','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
        this.form.setFieldsValue(pick(row,'planType','itemSlipType','itemSlip','businessType','theFactory','factoryText','disposalWay','theLocation','theLocationText','rawMaterialCode','rawMaterialText','rawMaterialUnit','wasteMaterialCode','wasteMaterialText','wasteMaterialUnit','numReceipts','alreadyDeliverStorage','alreadyReceivingStorage','deliverNum','deliverTime','deliverVoucher','projectNo','projectName','assetNo','assetText','realId','equipmentDeveui','objectInstruct','manufacturing','factoryNo','wasteVal','wasteReason','deliveryCreateBy','deliveryCreateTime','deliveryReceiveTime','projectContact','projectPhone','theContact','thePhone','warehouseContact','warehousePhone','demolitionPlan','engineeringAddress','systemNo','noteInformation','projectNoRemarks','rateFormation','plansCompleteTime','sendOrdersState','completeState','createTime','updateTime','createBy','updateBy','backup1','backup2','backup3','backup4','backup5'))
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
  .inupt_e{
    display: inline-block;
    width: 600px;
    margin-right: -215px;
    margin-left: -55px;
  }
</style>