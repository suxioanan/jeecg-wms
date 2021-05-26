<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="24">
            <a-form-item label="物料描述">
              <a-input placeholder="请输入物料描述" v-model="queryParam.materialDescribe"></a-input>
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
                  <a-button type="primary" @click="handleExportXls2('新品统计汇总')">导出统计</a-button>
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
        <!--<template slot="footer">
          合计：
          <span id="num" STYLE="position: absolute;left: 270px">
             {{length1}}
    </span>
          <span STYLE="position: absolute;left: 420px">
            {{weight1}}
    </span>
          <span STYLE="position: absolute;left: 585px">
            {{retrievalWeight1}}
    </span>

        </template>-->
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
    name: 'NewProducts',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JEllipsis,
      JDate
    },

    data() {
      return {
        description: '新品统计页面',
        form: this.$form.createForm(this),
        width: 800,
        loading: false,
        visible: false,
        warehousingNum1:0.00,
        warehouseOutNum1:0.00,
        engName1:'',
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
        queryParam: {planType:'新品'},
        // 表头
        columns: [
          {
            title: '废旧物料描述',
            align: 'center',
            width:300,
            dataIndex: 'materialDescribe',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '入库数量/中间库',
            align: 'center',
            width:213,
            dataIndex: 'warehousingNum'
          },
          {
            title: '出库数量/神州仓库',
            align: 'center',
            width:238,
            dataIndex: 'warehouseOutNum',
          },
          {
            title: '单位',
            align: 'center',
            width:79,
            dataIndex: 'itemText'
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
            dataIndex: 'engName',
            customRender: (value, row, index) => {
              const obj = {
                children: this.engName1,
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
          list: '/cable/plan3/selectNewproducts',
          exportXlsUrl2: '/cable/plan3/exportXls2'
        }
      }
    },
    mounted() {
      this.tableAddTotalRow(this.columns, this.dataSource)
    },
    computed: {
    },
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
        let numKey = 'materialDescribe'
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
        getAction(this.url.list, params).then((res) => {
          console.log(res)
          this.warehousingNum1=0.00;
          this.warehouseOutNum1=0.00;
          this.engName1="";
          for (let i = 0; i < res.result.records.length; i++) {
            //this['length1'+res.result.records[i].length1]=this.length1
                var pp1= res.result.records[i].warehousingNum*1;
                var pp2= res.result.records[i].warehouseOutNum*1;
                var pp4=res.result.records[i].engName;
                this.warehousingNum1=this.warehousingNum1+pp1;
                this.warehouseOutNum1=this.warehouseOutNum1+pp2;
                this.engName1=this.engName1+pp4+"\n,";
          }
          this.engName1 =this.engName1.substring(0,this.engName1.lastIndexOf(','));
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
      },
    },

    created() {
       this.loadData(1)
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>