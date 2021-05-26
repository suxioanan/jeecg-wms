<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">


          <a-col :md="4" :sm="24">
            <a-form-item label="废旧物料描述">
              <a-input placeholder="请输入废旧物料描述" v-model="queryParam.wasteMaterialText"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item
              label="起止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 400px;margin-right: -140px">
              <a-range-picker @change="onChange" />
<!--              v-model="queryParam.buildTime"-->
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="24">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                      <a-button style="margin-left: 8px" type="primary" icon="import" @click="showExportModal">导出反馈汇总</a-button>

                    <a-modal
                      v-model="visible"
                      :width=700
                      on-ok="handleCancel" style="margin-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form>

                      <a-form-item label="反馈日期" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                        style="display: inline-block;width: 400px;margin-right: -140px">
                    <j-date placeholder="输入反馈日期" v-model="queryParam.decommissioningDate"></j-date>
                  </a-form-item>
                        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     style="width: 795px;margin-left: -83px">
                    <a-input placeholder="输入备注" style="width: 400px" v-model="queryParam.remark"></a-input>
                  </a-form-item>
                      </a-form>
                      <!-- 自定义内容-END -->

                      <!-- 自定义页脚-begin -->
                <template slot="footer">
                  <a-button type="primary" @click="handleExportXls2('变电统计汇总')">导出统计</a-button>
                  <a-button @click="handleCancel">关闭</a-button>
                </template>
                      <!-- 自定义页脚-END -->
              </a-modal>

              </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force"
        >
        <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="25"/>
        </span>


        <!--        <template slot="footer">-->
<!--          合计：-->
<!--          <span id="num" STYLE="position: absolute;left: 400px">-->
<!--             {{stockNum1}}-->
<!--    </span>-->
<!--          <span STYLE="position: absolute;left: 630px">-->
<!--            {{receivingNum1}}-->
<!--    </span>-->

<!--        </template>-->

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>


      </a-table>
    </div>


  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { putAction,getAction } from '@/api/manage'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import { downFile } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'Substation',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JEllipsis,
      JDate
    },
    data() {
      return {
        description: '变电统计页面',
        form: this.$form.createForm(this),
        width: 800,
        loading: false,
        visible: false,
        stockNum1:0,
        receivingNum1:0,
        projectName1:"",
        dataSource:[],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        //查询条件
        queryParam: {planType:'配变电'},
        // 表头
        columns:[
          {
            title: '废旧物料描述',
            align: 'center',
            width:300,
            dataIndex: 'wasteMaterialText',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '入库数量/中间库',
            align: 'center',
            width:213,
            dataIndex: 'stockNum'
          },
          {
            title: '出库数量/神州仓库',
            align: 'center',
            width:238,
            dataIndex: 'receivingNum',
          },
          {
            title: '单位',
            align: 'center',
            width:79,
            dataIndex: 'rawMaterial'
          },
          {
            title: '反馈日期',
            align: 'center',
            dataIndex: ''
          }
          ,
          {
            title: '来源',
            align: 'center',
            width:400,
            dataIndex: 'projectName',
            customRender: (value, row, index) => {
              const obj = {
                children: this.projectName1,
                attrs: {},
              };
              if (index === 0) {
                obj.attrs.rowSpan = 10;
              }  if (index >= 1) {
                obj.attrs.rowSpan = 0;
              }
              if (index >= 2) {
                obj.attrs.colSpan = 0;
              }
              return obj;
            },
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: ''
          }
        ],
        url: {
          list: '/cable/plan1/selectSubstation',
          exportXlsUrl2: '/cable/plan1/exportXls2'
        }
      }
    },
    computed: {},
    methods: {
      // 导出反馈汇总
      handleExportXls2(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        if(this.buildTime[0] === undefined || this.buildTime[0] === "") return this.$message.warning('请选择起始止时间段!')
        // 构造参数
        let param = {
          remark:this.queryParam.remark,
          decommissioningDate:this.queryParam.decommissioningDate,
          planType:'配变电',
          beginTime: this.buildTime[0],
          endTime: this.buildTime[1],
        }
        console.log("起始时间》》》：",this.buildTime)
        console.log("导出参数参考》》》：",param)
        downFile(this.url.exportXlsUrl2, param).then((data) => {
          if (data) {
            this.$message.success("导出文件成功！")
            this.visible = false
          }else{
            this.$message.warning('文件下载失败！')
            this.visible = false
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
          } else {
            let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName + '.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) //下载完成移除元素
            window.URL.revokeObjectURL(url) //释放掉blob对象
            this.visible = false
          }
        })
      },
      /**
       * 时间控件 onChange
       */
      onChange(date, dateString) {
        console.log(date, dateString);
        this.buildTime = dateString;
        this.queryParam.beginTime = this.buildTime[0]
        this.queryParam.endTime = this.buildTime[1]
        this.loadData();
        console.log("this.queryParam",this.queryParam)
      },
      // 显示导出反馈汇总弹出框
      showExportModal() {
        this.visible = true
      },
      // 隐藏导出反馈汇总弹出框
      handleCancel() {
        this.visible = false
      },
      /** 表格增加合计行 */
      tableAddTotalRow(columns, dataSource) {
        let numKey = 'wasteMaterialText'
        let totalRow = { [numKey]: '合计' }
        columns.forEach(column => {
          let { key, dataIndex } = column
          if (![key, dataIndex].includes(numKey)) {

            let total = 0
            dataSource.forEach(data => {
              total = (total * 1000 + (/^\d+\.?\d?$/.test(data[dataIndex] * 1000) ? Number.parseInt(data[dataIndex] * 1000) : Number.NaN)) / 1000
              console.log(data[dataIndex], ':', (/^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN))
            })

            if (Number.isNaN(total)) {
              total = '-'
            }
            totalRow[dataIndex] = total
          }
        })

        dataSource.push(totalRow)
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        var params = this.getQueryParams()//查询条件
        this.loading = true
        console.log("params>>>:",params)
        getAction(this.url.list, params).then((res) => {
          this.stockNum1=0;
          this.receivingNum1=0;
          this.projectName1="";
          for (let i = 0; i < res.result.records.length; i++) {
            //this['length1'+res.result.records[i].length1]=this.length1
            var pp1= res.result.records[i].stockNum*1;
            var pp2= res.result.records[i].receivingNum*1;
            var pp4=res.result.records[i].projectName;
            this.stockNum1=this.stockNum1+pp1
            this.receivingNum1=this.receivingNum1+pp2
            this.projectName1=this.projectName1+pp4+"\n,";
          }
          this.projectName1 =this.projectName1.substring(0,this.projectName1.lastIndexOf(','));
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
            this.tableAddTotalRow(this.columns, this.dataSource)
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      }
    },
    created() {
      this.loadData(1)
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>