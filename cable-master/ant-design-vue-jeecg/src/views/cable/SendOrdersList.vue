<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <template>
          <div class="calender">
            <div class="top">
              <div class="top_date">
                <!--                  {{year}}年{{month}}月-->
                <a-form-item label="年度" style="display: inline-block;width: 140px;margin-top: 10px">
                  <a-select id="year" v-model="queryParam.year" placeholder="年度" style="width: 90px">
                    <template v-for="yeare in years">
                      <a-select-option v-bind:value="yeare">{{yeare}}</a-select-option>
                    </template>
                  </a-select>
                </a-form-item>
                <a-form-item label="月份" style="display: inline-block;width: 200px;margin-top: 10px">
                  <a-select id="month" v-model="queryParam.month" placeholder="月份" style="width: 80px">
                    <a-select-option value="1">1</a-select-option>
                    <a-select-option value="2">2</a-select-option>
                    <a-select-option value="3">3</a-select-option>
                    <a-select-option value="4">4</a-select-option>
                    <a-select-option value="5">5</a-select-option>
                    <a-select-option value="6">6</a-select-option>
                    <a-select-option value="7">7</a-select-option>
                    <a-select-option value="8">8</a-select-option>
                    <a-select-option value="9">9</a-select-option>
                    <a-select-option value="10">10</a-select-option>
                    <a-select-option value="11">11</a-select-option>
                    <a-select-option value="12">12</a-select-option>
                  </a-select>
                </a-form-item>
                <a-button type="primary" @click="selectTask" icon="search" style="margin-top: 10px">查询</a-button>
              </div>
              <div class="btn_wrap">
                <ul>
                  <li>
                    <a-button type="primary" @click="handleShowNextMonth">下个月</a-button>
                  </li>
                  <li>
                    <a-button type="primary" @click="handleShowToday">今天</a-button>
                  </li>
                  <li>
                    <a-button type="primary" @click="handleShowLastMonth">上个月</a-button>
                  </li>
                </ul>
              </div>
            </div>
            <div class="date_wrap">
              <ul class="week">
                <li>日</li>
                <li>一</li>
                <li>二</li>
                <li>三</li>
                <li>四</li>
                <li>五</li>
                <li>六</li>
              </ul>
              <ul class="day">
<!--                <li v-for="(item,index) in daysss">-->
<!--                  {{item}}-->
<!--                </li>-->
                <li v-for="(item,index) in days" :key=index :class="{now:nowLi==year.toString()+month.toString()+item}">
                  {{item}}
                  <span v-for="task in TheSameMonthTask" style="display: block">
                    <span v-if="item==task.d" style="display: inline-block;margin-left: -40px;margin-right: -40px">
                      <a @click="tasksList(task.d)" style="color: orange">{{task.cou}}辆车作业</a>
                    </span>
                  </span>
                </li>
              </ul>
            </div>
          </div>
        </template>
      </a-form>
    </div>
    <send-orders-modal ref="sendOrdersModal"></send-orders-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SendOrdersModal from './modules/SendOrdersModal'
  import {httpAction, getAction} from '@/api/manage'

  export default {
    name: "SendOrdersList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SendOrdersModal
    },
    data () {
      return {
        description: '任务计划表管理页面',
        //查询条件
        queryParam:{
          year:'',
          month:'',
        },
        year:'',
        month:'',
        days:[],
        dayss:[],
        daysss:[],
        nowLi:'',
        url: {
          list: "/cable/sendOrders/list",
          delete: "/cable/sendOrders/delete",
          deleteBatch: "/cable/sendOrders/deleteBatch",
          exportXlsUrl: "/cable/sendOrders/exportXls",
          importExcelUrl: "cable/sendOrders/importExcel",
          selectTheSameMonthSendOrders: "cable/sendOrders/selectTheSameMonthSendOrders",
          yearsList:"/cable/sendOrders/yearsList",
        },
        dictOptions:{},
        years:{},
        TheSameMonthTask:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods:{
      yearsList(){
        getAction(this.url.yearsList).then((res) => {
          if (res.success) {
            this.years = res.result;
          }
        })
      },
      tasksList(value){
        let months = this.month;
        months = months+'';
        if (months.length<2){
          months='0'+months;
        }
        let date = this.year+'-'+months+'-'+value;
        this.$refs.sendOrdersModal.agentSettings(date);
        this.$refs.sendOrdersModal.title = "作业车辆";
      },
      //根据年份与月份查询这个月每日任务数
      selectTask() {
        let yea = document.getElementById("year").innerText;
        let mont = document.getElementById("month").innerText;
        this.year = Number(yea);
        this.month = Number(mont);
        this.pushDays();
      },
      //控制当前日期显示特殊样式
      handleShowDateStyle(){
        let now = new Date()
        this.nowLi=now.getFullYear().toString()+(now.getMonth()+1).toString()+now.getDate().toString()
        //console.log(this.nowLi)
      },
      //得到当前年这个月分有多少天
      getDays(Y,M){
        let day = new Date(Y, M, 0).getDate()
        return day;
      },
      //得到当前年，这个月的一号是周几
      getWeek(Y,M){
        let now = new Date()
        now.setFullYear(this.year)
        now.setMonth(this.month-1)
        now.setDate(1);
        let week = now.getDay();
        return week;
      },
      pushDays(){
        this.days.length = 0;
        this.dayss.length = 0;
        this.daysss.length = 0;
        this.TheSameMonthTask = '';
        //将这个月多少天加入数组days
        let da = this.year+'-'+this.month+'-01'
        console.log('这个月')
        console.log(this.year)
        console.log(this.month)
        console.log('getDays')
        console.log(this.getDays(this.year,this.month))
        this.queryParam.year = this.year;
        this.queryParam.month = this.month;
        getAction(this.url.selectTheSameMonthSendOrders, {date: da}).then((res) => {
          if (res.success) {
            console.log('数据')
            console.log(res)
            this.TheSameMonthTask = res.result;
            console.log(this.TheSameMonthTask)
          }
        })
        for(let i = 1; i<=this.getDays(this.year,this.month);i++){
          this.days.push(i)
        }
        //将下个月要显示的天数加入days
        // for(let i = 1;i<=42-this.getDays(this.year,this.month)-this.getWeek(this.year,this.month);i++){
        //   this.days.push(i)
        // }
        //将上个月要显示的天数加入days
        console.log('上个月')
        console.log(this.year)
        console.log(this.month)
        console.log('getWeek')
        console.log(this.getWeek(this.year,this.month))
        for(let i=0;i<this.getWeek(this.year,this.month);i++){
          var lastMonthDays=this.getDays(this.year,this.month-1)
          this.days.unshift(null)
        }
        // for(let i=this.dayss.length-1;i==0 ;i--){
        //   this.daysss.push(this.dayss[i]);
        // }
      },
      getDate(){
        let now = new Date();
        this.year = now.getFullYear();
        this.month = now.getMonth()+1;
        this.pushDays();
      },
      handleShowNextMonth(){
        this.days=[];
        if(this.month<12){
          this.month=this.month+1;
          this.pushDays();
        }else{
          this.month= this.month=1;
          this.year=this.year+1;
          this.pushDays();
        }
      },
      handleShowToday(){
        this.days=[];
        let now = new Date();
        this.year=now.getFullYear();
        this.month=now.getMonth()+1;
        this.pushDays();
      },
      handleShowLastMonth(){
        this.days=[];
        if(this.month>1){
          this.month=this.month-1;
          this.pushDays();
        }else if( this.year>1970){
          this.month=12;
          this.year=this.year-1;
          this.pushDays();
        }else{
          alert("不能查找更远的日期")
        }
      }
    },
    mounted(){
      this.getDate();
      this.handleShowDateStyle();
      this.yearsList();
    }
  }
</script>
<style scoped>
  .calender{
    width: 80%;
    position: relative;
    margin: 0 auto;
    padding: 20px;
  }
  .top{
    width: 100%;
    position: relative;
    display: flex;
    border-bottom: 1px solid #777777;
    padding-bottom: 20px;
  }
  .top_date{
    width: 500px;
    text-align: center;
    font-size: 18px;
    margin-left: 15px;
  }
  .btn_wrap{
    flex: 1;
    text-align: center;
    margin-right: 15px;
  }
  .btn_wrap ul{
    display: flex;
    flex-direction: row-reverse
  }
  .btn_wrap ul li{
    text-align: center;
    list-style-type:none;
    padding: 10px 5px;
    line-height: 20px;
    cursor: pointer;
  }
  .date_wrap{
    position: relative;
  }
  .week{
    display: flex;
    flex-direction: row;
    padding: 20px;
    font-size: 16px;
  }
  .week li{
    text-align: center;
    list-style-type:none;
    width: 14%;
  }
  .day{
    display: flex;
    flex-direction: row;
    padding: 20px;
    font-size: 14px;
    flex-wrap: wrap;
    color: black;
  }
  .day li{
    list-style-type:none;
    width: 10%;
    padding: 25px 25px 0px 25px;
    height: 80px;
    box-sizing: border-box;
    border: 1px solid #777777;
    margin: 0px 2% 1.5% 2%;
    text-align: center;
    border-radius:5px
  }
  /*.day li:nth-child(n+8){*/
  /*  border-top:none;*/
  /*}*/
  /*.day li:nth-child(n+1){*/
  /*  border-right: none;*/
  /*}*/
  /*.day li:nth-child(7n){*/
  /*  border-right: 1px solid #777777*/
  /*}*/
  /*.day li:nth-child(7n){*/
  /*  border-right: 1px solid #777777*/
  /*}*/
  /*.day li:last-child{*/
  /*  width: 14.35%;*/
  /*  border-right: 1px solid #777777*/
  /*}*/
  .now{
    background: #d5e7f8;
    color: #0030fa;
    /*background: #f2f8fe;*/
    /*color: #2f54eb;*/
  }

</style>