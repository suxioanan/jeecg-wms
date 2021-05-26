<template>
  <!-- <div class="indexclass"> -->
  <div class="indextitle">
    <!-- 第一块大卡片 -->
    <a-row :span="24" :gutter="[29,20]" class="one">
      <a-col :span="6" class="one_one">
        <div class="one bottom_margin">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">当月项目结算</span>
          </div>
          <div class="span_hx"></div>
          <div class="one_one_body">
            <div class="yjs">
              <div>已结算</div>
              <div>{{ clsd }}</div>
            </div>
            <div class="wjs">
              <div>未结算</div>
              <div>{{ noclsd }}</div>
            </div>
          </div>
        </div>
        <div class="two">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">备品统计</span>
            <!--<a-select class="span_title_select" size="small" default-value="lucy" style="width: 120px"
                      @change="handleChange">
              <a-select-option value="jack">
                本周
              </a-select-option>
              <a-select-option value="lucy">
                本周
              </a-select-option>
              <a-select-option value="disabled" disabled>
                本周
              </a-select-option>
              <a-select-option value="Yiminghe">
                本周
              </a-select-option>
            </a-select>-->
            <a-range-picker class="span_title_select" size="small" @change="bptjTimeChange" style="width: 280px"/>
          </div>
          <div class="span_hx"></div>
          <div style="margin-top:20px">
            <div style="display:inline-block;float:right">
              <div
                style="height:14px;width:30px;background:linear-gradient(#FFC858, #FDAD51);display:inline-block;margin-right:10px"></div>
              <span style="display:inline-block;margin-right:10px">出</span>
              <div
                style="height:14px;width:30px;background:linear-gradient(#60A4DE, #21BEA2);display:inline-block;margin-right:10px"></div>
              <span style="display:inline-block;margin-right:10px">入</span>
            </div>
            <div style="height:200px;width:200px;" id="bptj"></div>
          </div>
        </div>
      </a-col>
      <a-col :span="11" class="one_two">
        <div class="one bottom_margin">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">配变电统计</span>
          </div>
          <div class="span_hx"></div>
          <div>
            <div style="height:200px;width:240px;display:inline-block;float:left" id="pbdtj"></div>
            <div style="width:492px;display:inline-block;margin-top:10px" class="one_two_table">
              <a-table bordered :columns="columns1" :data-source="data1" :pagination="{'item-render': itemRender}">
                <template v-slot:action>
                  <a @click="showPModal_pbdtj" style="color:#22BDA4">查看</a>
                </template>
              </a-table>
            </div>
            <!-- 配变电物料统计隐藏模块 -->
            <a-modal
              title="配变电物料统计"
              :visible="visible_pbdtj"
              :confirm-loading="confirmLoading"
              @ok="handleOk"
              @cancel="handleCancel_pbdtj"
            >
              <a-table :loadin="!xlUpdateData" id="pbdmk" bordered :columns="columns_pbd" :pagination="false"
                       :data-source="data_colum_pbd" :scroll="{ y: 240 }">
                <template v-slot:action>
                  <a @click="showPModal_pbdtj" style="color:#22BDA4">查看</a>
                </template>
              </a-table>
            </a-modal>
          </div>
        </div>
        <div class="two">
          <div class="span_title">
            <span class="span_title_icon"></span>
            <span class="span_title_span">新品统计</span>
          </div>
          <div class="span_hx"></div>
          <div>
            <div style="height:200px;width:240px;display:inline-block;float:left" id="xptjtb"></div>
            <div style="width:8.75rem;display:inline-block;margin-top:10px" class="two_two_table">
              <a-table bordered :columns="columns1" :components="components" :data-source="data1"
                       :pagination="{'item-render': itemRender}">
                <template v-slot:action>
                  <a @click="showPModal_xptj" style="color:#22BDA4">查看</a>
                </template>
              </a-table>

              <!-- 新品物料统计隐藏模块 -->
              <a-modal
                title="新品物料统计"
                :visible="visible_xptj"
                :confirm-loading="confirmLoading"
                @ok="handleOk"
                @cancel="handleCancel_xptj"
              >
                <a-table :loadin="!xlUpdateData" id="xpmk" bordered :columns="columns_xp" :pagination="false"
                         :data-source="data_colum_xp" :scroll="{ y: 240 }">
                  <template v-slot:action>
                    <a @click="showPModal_xptj" style="color:#22BDA4">查看</a>
                  </template>
                </a-table>
              </a-modal>

            </div>
          </div>
        </div>
      </a-col>
      <a-col :span="7" class="one_three">
        <div class="one bottom_margin">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">抢修统计</span>
          </div>
          <div class="span_hx"></div>
          <div class="one_three_body">
            <a-row>
              <a-col span='8'>
                <div class="one_three_body_one">
                  <div>
                    <span>{{ gztj }}</span>
                  </div>
                  <span>该周统计</span>
                </div>
              </a-col>
              <a-col span='8'>
                <div class="one_three_body_two">
                  <div>
                    <span>{{ gytj }}</span>
                  </div>
                  <span>该月统计</span>
                </div>
              </a-col>
              <a-col span='8'>
                <div class="one_three_body_three">
                  <div>
                    <span>{{ dntj }}</span>
                  </div>
                  <span>当年统计</span>
                </div>
              </a-col>
            </a-row>
          </div>
        </div>
        <div class="two">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">电缆统计</span>
          </div>
          <div class="span_hx"></div>
          <div style="height:auto">
            <div style="height:260px;width:260px;display:inline-block;float:left" id="dltj"></div>
            <div style="height:auto;width:148px;display:inline-block;margin-top:35px">
              <span>铜电揽收</span>
              <a-progress :percent="30" size="small" :show-info="false"
                          :strokeColor="{ from: '#66ADFD', to: '#4098FC' }"/>
              <span>铜电揽出</span>
              <a-progress :percent="30" size="small" :show-info="false"
                          :strokeColor="{ from: '#F78D7A', to: '#F57059' }"/>
              <span>铝电揽收</span>
              <a-progress :percent="30" size="small" :show-info="false"
                          :strokeColor="{ from: '#3CC9AF', to: '#0BBC9B' }"/>
              <span>铝电揽出</span>
              <a-progress :percent="30" size="small" :show-info="false"
                          :strokeColor="{ from: '#FF97B1', to: '#F99E6F' }"/>
            </div>
          </div>
        </div>
      </a-col>
    </a-row>

    <!-- 第二块大卡片 -->
    <a-row class="two" :span="24" :gutter="34">
      <a-col class="two_one" :span="8">
        <div class="one">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">仓库容积</span>
          </div>
          <div class="span_hx"></div>
          <div>
            <div style="height:260px;margin-top:35px" id="ckrj"></div>
          </div>
        </div>
      </a-col>
      <a-col class="two_two" :span="8">
        <div class="one">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">线路统计</span>
            <a-range-picker class="span_title_span" size="small" @change="xltjTimeChange"
                            style="width: 280px; margin-left: 10px;"/>
          </div>
          <div class="span_hx"></div>
          <div style="height:260px;margin-top:35px" id="xltj"></div>
        </div>
      </a-col>
      <a-col class="two_three" :span="8">
        <div class="one">
          <div class="span_title">
            <div class="span_title_icon"></div>
            <span class="span_title_span">临措统计</span>
            <!--<a-select class="span_title_select" size="small" default-value="lucy" style="width: 120px"
                      @change="handleChange">
              <a-select-option value="jack">
                本周
              </a-select-option>
              <a-select-option value="lucy">
                本周
              </a-select-option>
              <a-select-option value="disabled" disabled>
                本周
              </a-select-option>
              <a-select-option value="Yiminghe">
                本周
              </a-select-option>
            </a-select>-->
            <a-range-picker class="span_title_select" size="small" @change="lctjTimeChange" style="width: 280px"/>
          </div>
          <div class="span_hx"></div>
          <div style="height:260px;margin-top:35px" id="lctj"></div>
        </div>
      </a-col>
    </a-row>
    <!-- 欢迎进入Jeecg物流管理系统首页 -->
  </div>
  <!-- </div> -->
  <!--  <div>-->
  <!--    &lt;!&ndash; 主页内容-begin &ndash;&gt;-->
  <!--    <index-chart v-if="indexStyle==1"></index-chart>-->
  <!--    <index-bdc v-if="indexStyle==2"></index-bdc>-->
  <!--    <index-task v-if="indexStyle==3"></index-task>-->
  <!--    <div style="width: 100%;text-align: right;margin-top: 20px">-->
  <!--      请选择首页样式：-->
  <!--      <a-radio-group v-model="indexStyle">-->
  <!--        <a-radio :value="1">统计图表</a-radio>-->
  <!--        <a-radio :value="2">统计图表2</a-radio>-->
  <!--        <a-radio :value="3">任务表格</a-radio>-->
  <!--      </a-radio-group>-->
  <!--    </div>-->
  <!--    &lt;!&ndash; 主页内容-END &ndash;&gt;-->
  <!--  </div>-->
</template>

<script>
import IndexChart from './IndexChart'
import IndexTask from './IndexTask'
import IndexBdc from './IndexBdc'
import { Line, Column, GroupedColumn, PercentStackedColumn, StackedColumn, Donut } from '@antv/g2plot'
import * as G2 from '@antv/g2'
import { getAction } from '@api/manage'

export default {
  name: 'Analysis',
  components: {
    IndexChart,
    IndexTask,
    IndexBdc
  },
  data () {
    return {
      clsd: 75,
      noclsd: 29,
      gztj: 31,
      gytj: 52,
      dntj: 65,
      //下拉刷新准备状态
      xlUpdateData: true,
      //是否有更多数据
      otherData: false,
      visible_pbdtj: false,
      visible_xptj: false,
      indexStyle: 1,
      dsq: Object,
      columns1: [
        {
          title: '序号',
          dataIndex: 'xh',
          width: 54,
        },
        {
          title: '项目名称',
          dataIndex: 'date',
          width: 109,
        },
        {
          title: '完成状态',
          dataIndex: 'amount',
          width: 109,
        },
        {
          title: '入库状态',
          dataIndex: 'type',
          width: 109,
        },
        {
          title: '操作',
          dataIndex: 'note',
          width: 109,
          scopedSlots: { customRender: 'action' },
        }
      ],
      columns_pbd: [
        {
          title: '序号',
          dataIndex: 'xh',
          width: 58,
        },
        {
          title: '物料名称',
          dataIndex: 'name',
        },
        {
          title: '数量',
          dataIndex: 'num',
        },
        {
          title: '单位',
          dataIndex: 'dw',
        }
      ],
      columns_xp: [
        {
          title: '序号',
          dataIndex: 'xh',
          width: 58,
        },
        {
          title: '物料名称',
          dataIndex: 'name',
        },
        {
          title: '数量',
          dataIndex: 'num',
        },
        {
          title: '单位',
          dataIndex: 'dw',
        }
      ],
      data_colum_pbd: [
        {},
      ],
      data_colum_xp: [
        {},
      ],
      data1: [
        {},
        {},
        {},
        {},
      ],
      bptj_chart: null, // 备品统计所需 G2 charts
      bptj_data: [
        { country: '其他', type: '出', value: 0 },
        { country: '其他', type: '入', value: 0 },
        { country: '屏', type: '出', value: 0 },
        { country: '屏', type: '入', value: 0 },
        { country: '箱变', type: '出', value: 0 },
        { country: '箱变', type: '入', value: 0 },
        { country: '柜子', type: '出', value: 0 },
        { country: '柜子', type: '入', value: 0 },
        { country: '变压器', type: '出', value: 0 },
        { country: '变压器', type: '入', value: 0 },
      ], // 备品统计所需数据源
      bptj_beginTime: null, // 备品统计查询条件 开始日期
      bptj_endTime: null, // 备品统计查询条件 结束日期
      xltj_columnPlot: null, // 线路统计所需 分组柱状图 --- GroupedColumn
      xltj_data: [
        { name: '计划数量', 物料简称: '导线', 数量: 0 },
        { name: '计划数量', 物料简称: '开关', 数量: 0 },
        { name: '计划数量', 物料简称: '变压器', 数量: 0 },
        { name: '计划数量', 物料简称: '其他', 数量: 0 },
        { name: '入库', 物料简称: '导线', 数量: 0 },
        { name: '入库', 物料简称: '开关', 数量: 0 },
        { name: '入库', 物料简称: '变压器', 数量: 0 },
        { name: '入库', 物料简称: '其他', 数量: 0 }
      ], // 线路统计所需数据源
      xltj_beginTime: null, // 线路统计查询条件 开始日期
      xltj_endTime: null, // 线路统计查询条件 结束日期
      lctj_columnPlot: null, // 临措统计所需 堆叠柱状图 -- StackedColumn
      lctj_data: [
        { states: '出', name: '电缆', value: 0, },
        { states: '出', name: '电缆配件', value: 0, },
        { states: '出', name: '变压器', value: 0, },
        { states: '出', name: '其他', value: 0, },
        { states: '入', name: '电缆', value: 0, },
        { states: '入', name: '电缆配件', value: 0, },
        { states: '入', name: '变压器', value: 0, },
        { states: '入', name: '其他', value: 0 }
      ], // 临措统计所需数据源
      lctj_beginTime: null, // 临措统计查询条件 开始日期
      lctj_endTime: null, // 临措统计查询条件 结束日期
    }
  },
  mounted () {
    this.bptj()
    this.ckrj()
    this.xltj()
    this.lctj()
    this.dltj()
    this.pbdtj()
    this.xptjtb()
  },
  created () {

  },
  methods: {
    /** 这里是配变电统计查看操作的打开方法  */
    showPModal_pbdtj () {
      this.visible_pbdtj = true
      //清除以前的定时器
      window.clearInterval(this.dsq)
      //下拉刷新已准备
      this.xlUpdateData = true,
        //创建定时器并记录
        this.dsq = setInterval(() => {
          var boxHeight = document.getElementById('pbdmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].clientHeight
          var boxAllHeight = document.getElementById('pbdmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].getElementsByTagName('table')[0].clientHeight
          var scrollTop = document.getElementById('pbdmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].scrollTop

          //如果隐藏，则清除定时器
          if (!this.visible_pbdtj) {
            window.clearInterval(this.dsq)
            return
          }
          if (boxHeight + scrollTop == boxAllHeight && this.xlUpdateData) {
            //这里填写数据插入方法   插入后记得把this.xlUpdateData状态回退为true
            this.xlUpdateData = false
            this.data_colum_pbd.push({})
            this.xlUpdateData = true
          }
        }, 500)
    },
    handleCancel_pbdtj (e) {
      this.visible_pbdtj = false
    },
    /** 这里是配变电统计查看操作的打开方法  */
    /** 这里是新品统计查看操作的打开方法  */
    showPModal_xptj () {
      this.visible_xptj = true
      //清除以前的定时器
      window.clearInterval(this.dsq)
      //下拉刷新已准备
      this.xlUpdateData = true,
        //创建定时器并记录
        this.dsq = setInterval(() => {
          var boxHeight = document.getElementById('xpmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].clientHeight
          var boxAllHeight = document.getElementById('xpmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].getElementsByTagName('table')[0].clientHeight
          var scrollTop = document.getElementById('xpmk').getElementsByClassName('ant-table-scroll')[0].getElementsByClassName('ant-table-body')[0].scrollTop

          //如果隐藏，则清除定时器
          if (!this.visible_xptj) {
            window.clearInterval(this.dsq)
            return
          }
          if (boxHeight + scrollTop == boxAllHeight && this.xlUpdateData) {
            console.log('定时器')
            //这里填写数据插入方法   插入后记得把this.xlUpdateData状态回退为true
            this.xlUpdateData = false
            this.data_colum_xp.push({})
            this.xlUpdateData = true
          }
        }, 500)
    },
    handleCancel_xptj () {
      this.visible_xptj = false
    },
    /** 这里是新品统计查看操作的打开方法  */

    // 备品统计 bai 2020/9/16
    bptj () {
      let _this = this
      let req = {
        'beginTime': _this.bptj_beginTime,
        'endTime': _this.bptj_endTime
      }
      getAction('/index/getBTPJList', req).then(res => {
        console.log('首页备品统计模块', res)
        for (let i = 0; i < _this.bptj_data.length; i++) {
          if (_this.bptj_data[i].country == '变压器') {
            if (_this.bptj_data[i].type == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  _this.bptj_data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  _this.bptj_data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (_this.bptj_data[i].country == '柜子') {
            if (_this.bptj_data[i].type == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '柜子') {
                  _this.bptj_data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '柜子') {
                  _this.bptj_data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (_this.bptj_data[i].country == '箱变') {
            if (_this.bptj_data[i].type == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '箱变') {
                  _this.bptj_data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '箱变') {
                  _this.bptj_data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (_this.bptj_data[i].country == '屏') {
            if (_this.bptj_data[i].type == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '屏') {
                  _this.bptj_data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '屏') {
                  _this.bptj_data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (_this.bptj_data[i].country == '其他') {
            if (_this.bptj_data[i].type == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  _this.bptj_data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  _this.bptj_data[i].value = res.result[j].deliverNum
                }
              }
            }
          }
        }
        console.log('备品统计图所需数据源为', _this.bptj_data)

        _this.bptj_chart = new G2.Chart({
          container: 'bptj',
          width: 300,
          height: 300
        })
        _this.bptj_chart.data(_this.bptj_data)
        _this.bptj_chart.axis('value', false)
        _this.bptj_chart.scale('value', {
          ticks: [0, 20, 40, 60, 80, 100]
        })
        _this.bptj_chart.facet('mirror', {
          fields: ['type'],
          transpose: true,
          showTitle: false,
          eachView: (view, facet) => {
            console.log('备品统计模块>>>>>>', view, facet)
            const facetIndex = facet.columnIndex
            view.axis('country', false)
            const color = (facetIndex === 0) ? 'l(0) 0:#FDAA50 1:#FFCB59' : 'l(0) 0:#63A3E0 1:#1CC09E'
            if (facetIndex === 0) {
              view.interval().position('country*value').color(color)
                .size(18)
            } else {
              view.interval().position('country*value').color(color)
                .size(18)
            }
          }
        })
        _this.bptj_chart.annotation().text({
          position: ['60%', '5%'],
          content: '变压器',
          style: {
            fill: '#656D92',
            fontSize: 14
          }
        })
        _this.bptj_chart.annotation().text({
          position: ['60%', '22%'],
          content: '柜子',
          style: {
            fill: '#656D92',
            fontSize: 14
          }
        })
        _this.bptj_chart.annotation().text({
          position: ['60%', '41%'],
          content: '箱变',
          style: {
            fill: '#656D92',
            fontSize: 14
          }
        })
        _this.bptj_chart.annotation().text({
          position: ['60%', '59%'],
          content: '屏',
          style: {
            fill: '#656D92',
            fontSize: 14
          }
        })
        _this.bptj_chart.annotation().text({
          position: ['60%', '79%'],
          content: '其他',
          style: {
            fill: '#656D92',
            fontSize: 14
          }
        })
        _this.bptj_chart.annotation().line({
          start: ['50%', '0%'],
          end: ['50%', '90%']
        })
        _this.bptj_chart.render()
        /**
         * G2.chart 图标绑定单击事件
         * @param ev 单击的图表参数
         */
        _this.bptj_chart.on('click', ev => {
          console.log('备品统计 echart 单击事件入参>>>', ev)
          _this.$router.push({
            name: 'cable-OutPutWarehouseList', // 跳转组件名称
            query: { // 携带参数
              beginTime: _this.bptj_beginTime,
              endTime: _this.bptj_endTime,
              planType: '备品'
            }
          })
          // window.location.href = 'http://www.baidu.com' // Vue 跳转至外界页面
        })
      }).catch(err => {
        console.log(err)
      })
    },
    // 备品统计时间 change 事件 bai 2020/9/16
    bptjTimeChange (date, dateString) {
      let _this = this
      console.log('备品统计时间 change 事件', dateString)
      _this.bptj_beginTime = dateString[0]
      _this.bptj_endTime = dateString[1]
      for (let i = 0; i < _this.bptj_data.length; i++) {
        _this.bptj_data[i].value = 0 // 将备品统计中的数量设置为 0
      }
      _this.bptj_chart.destroy() // 销毁备品统计图表
      _this.bptj()
    },
    // 线路统计 bai 2020/9/17
    xltj () {
      let _this = this
      let data = _this.xltj_data
      let req = {
        'beginTime': _this.xltj_beginTime,
        'endTime': _this.xltj_endTime
      }
      getAction('/index/getXLTJList', req).then(res => {
        console.log('首页线路统计模块', res)
        for (let i = 0; i < data.length; i++) {
          if (data[i].物料简称 == '导线') {
            if (data[i].name == '计划数量') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '导线') {
                  data[i].数量 = res.result[j].numReceipts
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '导线') {
                  data[i].数量 = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].物料简称 == '开关') {
            if (data[i].name == '计划数量') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '开关') {
                  data[i].数量 = res.result[j].numReceipts
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '开关') {
                  data[i].数量 = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].物料简称 == '变压器') {
            if (data[i].name == '计划数量') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  data[i].数量 = res.result[j].numReceipts
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  data[i].数量 = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].物料简称 == '其他') {
            if (data[i].name == '计划数量') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  data[i].数量 = res.result[j].numReceipts
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  data[i].数量 = res.result[j].deliverNum
                }
              }
            }
          }
        }
        console.log('线路统计图所需数据源为', data)

        // GroupedColumn 是分组柱状图
        _this.xltj_columnPlot = new GroupedColumn(document.getElementById('xltj'), {
          title: {
            visible: false,
          },
          forceFit: true,
          data,
          xField: '物料简称',
          yField: '数量',
          color: ['#FFC858', '#09C5CE'],
          columnStyle: (d) => {
            if (d === '入库') {
              return { fill: 'l(90) 0:#09C5CE 1:#4DDAC5' }
            } else if (d === '计划数量') {
              return { fill: 'l(90) 0:#FFC858 1:#FDAC51' }
            }
          },
          yAxis: {
            min: 0,
            max: 100,
            tickInterval: 20,
            line: {
              visible: true
            },
            title: {
              visible: false
            },
            grid: {
              visible: false
            }
          },
          xAxis: {
            title: {
              visible: false
            }
          },
          legend: {
            visible: true,
            position: 'top-right',
            title: {
              style: {
                fontSize: 14
              }
            },
            flipPage: true
          },
          groupField: 'name',
        })
        _this.xltj_columnPlot.render()
      }).catch(err => {
        console.log(err)
      })
    },
    // 线路统计时间 change 事件 bai 2020/9/17
    xltjTimeChange (date, dateString) {
      let _this = this
      console.log('线路统计时间 change 事件', dateString)
      _this.xltj_beginTime = dateString[0]
      _this.xltj_endTime = dateString[1]
      for (let i = 0; i < _this.xltj_data.length; i++) {
        _this.xltj_data[i].数量 = 0  // 将线路统计中的数量设置为 0
      }
      _this.xltj_columnPlot.destroy() // 销毁线路统计柱状图
      _this.xltj()
    },
    // 临措统计 bai 2020/9/17
    lctj () {
      let _this = this
      let data = _this.lctj_data
      let req = {
        'beginTime': _this.lctj_beginTime,
        'endTime': _this.lctj_endTime
      }
      getAction('/index/getLCTJList', req).then(res => {
        console.log('首页临措统计模块', res)
        for (let i = 0; i < data.length; i++) {
          if (data[i].name == '电缆') {
            if (data[i].states == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '电缆') {
                  data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '电缆') {
                  data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].name == '电缆配件') {
            if (data[i].states == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '电缆配件') {
                  data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '电缆配件') {
                  data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].name == '变压器') {
            if (data[i].states == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '变压器') {
                  data[i].value = res.result[j].deliverNum
                }
              }
            }
          } else if (data[i].name == '其他') {
            if (data[i].states == '出') {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  data[i].value = res.result[j].receivingNum
                }
              }
            } else {
              for (let j = 0; j < res.result.length; j++) {
                if (res.result[j].backup1 == '其他') {
                  data[i].value = res.result[j].deliverNum
                }
              }
            }
          }
        }
        console.log('临措统计图所需数据源为', data)

        _this.lctj_columnPlot = new StackedColumn(document.getElementById('lctj'), {
          title: {
            visible: false
          },
          forceFit: true,
          data,
          xField: 'name',
          yField: 'value',
          stackField: 'states',
          columnSize: 14,
          color: ['#CEE8E8', '#63A3E0'],
          columnStyle: (b) => {
            if (b === '入') {
              return { fill: 'l(90) 0:#63A3E0 1:#1CC09E' }
            } else if (b === '出') {
              return { fill: '#CEE8E8' }
            }
          },
          yAxis: {
            line: {
              visible: true
            },
            tickLine: {
              visible: true
            },
            label: {
              visible: true,
            },
            title: {
              visible: false
            }
          },
          xAxis: {
            title: {
              visible: false
            }
          },
          legend: {
            position: 'top-right',
          }
        })
        _this.lctj_columnPlot.render()
      }).catch(err => {
        console.log(err)
      })
    },
    // 临措统计时间 change 事件 bai 2020/9/17
    lctjTimeChange (date, dateString) {
      let _this = this
      console.log('临措统计时间 change 事件', dateString)
      _this.lctj_beginTime = dateString[0]
      _this.lctj_endTime = dateString[1]
      for (let i = 0; i < _this.lctj_data.length; i++) {
        _this.lctj_data[i].value = 0  // 将临措统计中的数量设置为 0
      }
      _this.lctj_columnPlot.destroy() // 销毁临措统计堆叠柱状图
      _this.lctj()
    },

    ckrj () {
      const data = [
        {
          type: '仓库1',
          sales: 38,
        },
        {
          type: '仓库2',
          sales: 38,
        },
        {
          type: '仓库3',
          sales: 38,
        },
        {
          type: '仓库4',
          sales: 80,
        },
      ]
      const columnPlot = new Column(document.getElementById('ckrj'), {
        forceFit: true,
        data,
        padding: 'auto',
        data,
        xField: 'type',
        // 配合颜色映射，指定多值
        colorField: 'type',
        color: ['#CEE8E8', '#4098FC', '#FFA348', '#FF1B41'],
        yField: 'sales',
        columnSize: 30,
        meta: {
          type: {
            alias: '类别',
          },
          sales: {
            alias: '销售额(万)',
          },
        },
        yAxis: {
          min: 0,
          max: 100,
          tickInterval: 20,
          line: {
            visible: true
          },
          title: {
            visible: false
          },
          grid: {
            visible: false
          }
        },
        xAxis: {
          title: {
            visible: false
          }
        },
        legend: {
          visible: true,
          position: 'top-right',
          marker: {
            style: {
              r: 5
            }
          },
          title: {
            style: {
              fontSize: 14
            }
          }
        },
      })
      columnPlot.render()
    },
    dltj () {
      const data = [
        {
          type: '铜电揽收',
          value: 0.8,
        },
        {
          type: '铜电揽出',
          value: 0.6,
        },
        {
          type: '铝电揽收',
          value: 0.3,
        },
        {
          type: '铝电揽出',
          value: 0.4,
        },
      ]

      const ringPlot = new Donut(document.getElementById('dltj'), {
        forceFit: true,
        radius: 0.8,
        innerRadius: 0.6,
        data,
        color: ['#4098FC', '#F57059', '#0BBC9B', '#F99E6F'],
        angleField: 'value',
        colorField: 'type',

        label: {
          visible: false
        },
        statistic: {
          htmlContent: (e) => {
            return '电缆统计'
          }
        },
        legend: {
          visible: false
        },
      })

      ringPlot.render()
    },
    pbdtj () {
      const data = [
        {
          type: '未完成',
          value: 0.8,
        },
        {
          type: '已完成',
          value: 0.2,
        },
      ]

      const ringPlot = new Donut(document.getElementById('pbdtj'), {
        forceFit: true,
        radius: 0.8,
        innerRadius: 0.8,
        data,
        color: ['#F5F6FA', '#FA7252'],
        angleField: 'value',
        colorField: 'type',
        legend: {
          visible: false
        },
        label: {
          visible: true,
          type: 'outer-center',
          formatter: (text, item) => `${item._origin.type}`,
        },
        statistic: {
          visible: false
        }
      })

      ringPlot.render()
    },
    xptjtb () {
      const data = [
        {
          type: '未完成',
          value: 0.8,
        },
        {
          type: '已完成',
          value: 0.2,
        },
      ]

      const ringPlot = new Donut(document.getElementById('xptjtb'), {
        forceFit: true,
        radius: 0.8,
        innerRadius: 0.8,
        data,
        color: ['#F5F6FA', '#FA7252'],
        angleField: 'value',
        colorField: 'type',
        legend: {
          visible: false
        },
        label: {
          visible: true,
          type: 'outer-center',
          formatter: (text, item) => `${item._origin.type}`,
        },
        statistic: {
          visible: false
        }
      })

      ringPlot.render()
    },
    itemRender (current, type, originalElement) {
      if (type === 'prev') {
        return <a>上一页</a>
      } else if (type === 'next') {
        return <a>下一页</a>
      }
      return originalElement
    },
  },
}
</script>
<style>
.indexclass {
  background-size: 100%;
}

.indextitle {
  width: 100%;
  /* padding-bottom: 24.5%;
  padding-top: 20%; */
}

.span_hx {
  width: auto;
  margin-top: 8px;
  margin-left: 15px;
  margin-right: 15px;
  border: 1px solid #F2F3F8;
}

.span_title {
  height: 31px;
}

.span_title .span_title_icon {
  height: 15px;
  width: 3px;
  background-color: #6F7799;
  display: inline-block;
  vertical-align: middle;
  margin-left: 16px;
  margin-top: 9px;
}

.span_title .span_title_span {
  display: inline-block;
  vertical-align: middle;
  color: #6F7799;
  font-size: 16px;
  margin-left: 8px;
  margin-top: 8px;
}

.one {
  margin-left: 10px;
}

.one .bottom_margin {
  margin-bottom: 24px;
}

/* 卡片圆角 */

/* 第一大块的第一列 */
.one_one .one {
  height: 225px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.one_one .two {
  height: 339px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.one_one .two .span_title_icon {
  margin-top: -3px;
}

.one_one .two .span_title_span {
  margin-top: -4px;
}

.one_one .two .span_title_select {
  margin-left: 10px;
  margin-top: 10px;
}

.one_one .one_one_body {
  position: relative;
  margin-top: 25px;
}

.one_one .one_one_body .yjs {
  display: inline-block;
  position: absolute;
  background-image: url('img/redone.png');
  background-size: 100%;
  top: 20%;
  left: 15%;
  width: 112px;
  height: 112px;
}

.one_one .one_one_body .yjs div:nth-child(1) {
  color: #70789A;
  font-size: 16px;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.one_one .one_one_body .yjs div:nth-child(2) {
  color: #FA7A5C;
  font-size: 29.9px;
  position: absolute;
  top: 65%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.one_one .one_one_body .wjs {
  display: inline-block;
  position: absolute;
  background-image: url('img/greenone.png');
  background-size: 100%;
  right: 15%;
  width: 112px;
  height: 112px;
}

.one_one .one_one_body .wjs div:nth-child(1) {
  color: #70789A;
  font-size: 16px;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.one_one .one_one_body .wjs div:nth-child(2) {
  color: #FA7A5C;
  font-size: 29.9px;
  position: absolute;
  top: 65%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/* 第一大块的第二列 */
.one_two .one {
  height: 283px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.one_two .two {
  height: 283px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

/* 第一列表格 */
.one_two_table .ant-table-thead th {
  height: 34px;
  padding: 0;
  text-align: center;
  background: #EBEDF2;
  border-top: 1px solid #B5B9CD;
  border-left: 1px solid #B5B9CD;
  border-bottom: 1px solid #B5B9CD;
}

.one_two_table .ant-table-thead tr th:nth-last-child(1) {
  border-right: 1px solid #B5B9CD;
}

.one_two_table .ant-table-tbody tr {
  height: 33px;
  text-align: center;
  border: 1px solid #e8e8e8
}

.one_two_table .ant-table-tbody tr td {
  padding: 0;
  text-align: center;
}

.one_two_table .ant-table-tbody tr td:nth-child(1) {
  background-color: #EBEDF2;
  border-bottom: 1px solid #B5B9CD;
  border-left: 1px solid #B5B9CD;
  border-right: 1px solid #B5B9CD;
}

.one_two_table .ant-pagination li {
  height: 21px;
  line-height: 21px;
}

.one_two_table .ant-pagination .ant-pagination-disabled {
  border: 1px solid #B5B9CD;
  height: 21px;
  width: 47px;
}

.one_two_table .ant-pagination .ant-pagination-disabled a {
  font-size: 11px;
}

.one_two_table .ant-pagination li {
  height: 21px;
  line-height: 21px;
}

.one_two_table .ant-pagination .ant-pagination-item-active {
  background-color: #46D8C6;
  border: 0;
}

.one_two_table .ant-pagination .ant-pagination-item-active a {
  color: #FFFFFF;
}


/* 第二列表格 */
.two_two_table .ant-table-thead th {
  height: 34px;
  padding: 0;
  text-align: center;
  background: #EBEDF2;
  border-top: 1px solid #B5B9CD;
  border-left: 1px solid #B5B9CD;
  border-bottom: 1px solid #B5B9CD;
}

.two_two_table .ant-table-thead tr th:nth-last-child(1) {
  border-right: 1px solid #B5B9CD;
}

.two_two_table .ant-table-tbody tr {
  height: 33px;
  text-align: center;
  border: 1px solid #e8e8e8
}

.two_two_table .ant-table-tbody tr td {
  padding: 0;
  text-align: center;
}

.two_two_table .ant-table-tbody tr td:nth-child(1) {
  background-color: #EBEDF2;
  border-bottom: 1px solid #B5B9CD;
  border-left: 1px solid #B5B9CD;
  border-right: 1px solid #B5B9CD;
}


.two_two_table .ant-pagination li {
  height: 21px;
  line-height: 21px;
}

.two_two_table .ant-pagination .ant-pagination-disabled {
  border: 1px solid #B5B9CD;
  height: 21px;
  width: 47px;
}

.two_two_table .ant-pagination .ant-pagination-disabled a {
  font-size: 11px;
}

.two_two_table .ant-pagination .ant-pagination-item-active {
  background-color: #46D8C6;
  border: 0;
}

.two_two_table .ant-pagination .ant-pagination-item-active a {
  color: #FFFFFF;
}


/* 第一大块的第三列 */
.one_three .one {
  height: 245px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.one_three .two {
  height: 324px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.one_three .one_three_body {
  padding-top: 21px;
  padding-left: 49px;
}

.one_three .one_three_body .one_three_body_one div {
  background-image: url('img/bulethree.png');
  height: 92px;
  width: 92px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.one_three .one_three_body .one_three_body_one div span {
  color: #FFFFFF;
  font-size: 18px;
  margin: 18px 0 0 15px;
}

.one_three .one_three_body .one_three_body_one > span {
  color: #5B6388;
  font-size: 16px;
  line-height: 48px;
  margin-left: 20px;
}

.one_three .one_three_body .one_three_body_two div {
  background-image: url('img/yellowthree.png');
  height: 92px;
  width: 92px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.one_three .one_three_body .one_three_body_two div span {
  color: #FFFFFF;
  font-size: 18px;
  margin: 18px 0 0 15px;
}

.one_three .one_three_body .one_three_body_two > span {
  color: #5B6388;
  font-size: 16px;
  line-height: 48px;
  margin-left: 20px;
}

.one_three .one_three_body .one_three_body_three div {
  background-image: url('img/redthree.png');
  height: 92px;
  width: 92px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.one_three .one_three_body .one_three_body_three div span {
  color: #FFFFFF;
  font-size: 18px;
  margin: 18px 0 0 15px;
}

.one_three .one_three_body .one_three_body_three > span {
  color: #5B6388;
  font-size: 16px;
  line-height: 48px;
  margin-left: 20px;
}

/* 第二大块 */
.two {
  margin-bottom: 17px;
  border-radius: 8px;
  margin-left: 10px;
}

/* 第二大块的第一列 */
.two_one .one {
  height: 324px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

/* 第二大块的第二列 */
.two_two .one {
  height: 324px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

/* 第二大块的第三列 */
.two_three .one {
  height: 324px;
  border-radius: 8px;
  background-color: #FFFFFF;
}

.two_three .one .span_title_icon {
  margin-top: -3px;
}

.two_three .one .span_title_span {
  margin-top: -4px;
}

.two_three .one .span_title_select {
  margin-left: 10px;
  margin-top: 10px;
}
</style>