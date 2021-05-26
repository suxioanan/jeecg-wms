<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="12">
            <a-form-item label="用户名">
              <a-input placeholder="请输入用户名" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="员工姓名">
                <a-input placeholder="请输入员工姓名" v-model="queryParam.realname"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="7">
              <a-form-item label="部门">
                <a-select v-model="queryParam.departIds" placeholder="请选择部门">
                  <template v-for="warehouse in warehouseLists">
                    <a-select-option v-bind:value="warehouse.itemValue">{{warehouse.itemText}}</a-select-option>
                  </template>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="用户状态">
                <a-select v-model="queryParam.status" placeholder="请选择用户状态">
                  <a-select-option value="1">正常</a-select-option>
                  <a-select-option value="2">禁用</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
               <a-button @click="handleAdd" type="primary" icon="plus" style="margin-left: 8px">添加用户</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">

      <!--      <a-button type="primary" icon="hdd" @click="recycleBinVisible=true">回收站</a-button>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel"/>
            删除
          </a-menu-item>
          <a-menu-item key="2">
            <a-icon type="lock" @click="batchFrozen('2')"/>
            冻结
          </a-menu-item>
          <a-menu-item key="3">
            <a-icon type="unlock" @click="batchFrozen('1')"/>
            解冻
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <!-- 字符串超长截取省略号显示 -->
        <span slot="address" slot-scope="text">
          <j-ellipsis :value="text" :length="9"/>
        </span>
        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
         <!-- <a @click="handleEdit(record)" v-has="'user:edit'">编辑</a>-->
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
           <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
           <a-divider type="vertical"/>
            <template v-if="record.status==1">
                <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id,2,record.username)">
                  <a>冻结</a>
                </a-popconfirm>
              </template>

              <template v-if="record.status==2">
                <a-popconfirm title="确定正常吗?" @confirm="() => handleFrozen(record.id,1,record.username)">
                  <a>正常</a>
                </a-popconfirm>
              </template>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <user-modal ref="modalForm" @ok="modalFormOk"></user-modal>

    <password-modal ref="passwordmodal" @ok="passwordModalOk"></password-modal>

    <sys-user-agent-modal ref="sysUserAgentModal"></sys-user-agent-modal>

    <!-- 用户回收站 -->
    <user-recycle-bin-modal :visible.sync="recycleBinVisible" @ok="modalFormOk"/>

  </a-card>
</template>

<script>
  import UserModal from './modules/UserModal'
  import PasswordModal from './modules/PasswordModal'
  import { frozenBatch } from '@/api/api'
  import { getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysUserAgentModal from './modules/SysUserAgentModal'
  import JInput from '@/components/jeecg/JInput'
  import UserRecycleBinModal from './modules/UserRecycleBinModal'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'UserList',
    mixins: [JeecgListMixin],
    components: {
      SysUserAgentModal,
      UserModal,
      PasswordModal,
      JInput,
      UserRecycleBinModal,
      JEllipsis
    },
    data() {
      return {
        description: '这是用户管理页面',
        queryParam: {},
        recycleBinVisible: false,
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title: '用户名',
            align: 'center',
            dataIndex: 'username',
            width: 120,
          },
          {
            title: '员工姓名',
            align: 'center',
            width: 100,
            dataIndex: 'realname'
          },

          {
            title: '性别',
            align: 'center',
            width: 80,
            dataIndex: 'sex_dictText',
          },
          {
            title: '部门',
            align: 'center',
            width: 100,
            dataIndex: 'departIds_dictText'
          },
          {
            title: '入职时间',
            align: 'center',
            width: 100,
            dataIndex: 'ruzhiTime'
          },
          {
            title: '离职时间',
            align: 'center',
            width: 100,
            dataIndex: 'resignationsTime'
          },
          {
            title: '手机号码',
            align: 'center',
            width: 100,
            dataIndex: 'phone'
          },

          {
            title: '状态',
            align: 'center',
            width: 80,
            dataIndex: 'status_dictText'
          },
          {
            title: '家庭住址',
            align: 'center',
            width: 110,
            dataIndex: 'address',
            scopedSlots: { customRender: 'address' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            align: 'center',
            width: 170
          }

        ],
        url: {
          syncUser: '/process/extActProcess/doSyncUser',
          bumenList: '/cable/testdata/bumenList',
          list: '/sys/user/list',
          delete: '/sys/user/delete',
          deleteBatch: '/sys/user/deleteBatch',
          exportXlsUrl: '/sys/user/exportXls',
          importExcelUrl: 'sys/user/importExcel'
        },
        warehouseLists: []
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      warehouseList() {
        this.warehouseLists = ''
        getAction(this.url.bumenList).then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
          }
        })
      },
      batchFrozen: function(status) {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！')
          return false
        } else {
          let ids = ''
          let that = this
          let isAdmin = false
          that.selectionRows.forEach(function(row) {
            if (row.username == 'admin') {
              isAdmin = true
            }
          })
          if (isAdmin) {
            that.$message.warning('管理员账号不允许此操作,请重新选择！')
            return
          }
          that.selectedRowKeys.forEach(function(val) {
            ids += val + ','
          })
          that.$confirm({
            title: '确认操作',
            content: '是否' + (status == 1 ? '解冻' : '冻结') + '选中账号?',
            onOk: function() {
              frozenBatch({ ids: ids, status: status }).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadData()
                  that.onClearSelected()
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      handleMenuClick(e) {
        if (e.key == 1) {
          this.batchDel()
        } else if (e.key == 2) {
          this.batchFrozen(2)
        } else if (e.key == 3) {
          this.batchFrozen(1)
        }
      },
      handleFrozen: function(id, status, username) {
        let that = this
        //TODO 后台校验管理员角色
        if ('admin' == username) {
          that.$message.warning('管理员账号不允许此操作！')
          return
        }
        frozenBatch({ ids: id, status: status }).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.loadData()
          } else {
            that.$message.warning(res.message)
          }
        })
      },
      handleChangePassword(username) {
        this.$refs.passwordmodal.show(username)
      },
      handleAgentSettings(username) {
        this.$refs.sysUserAgentModal.agentSettings(username)
        this.$refs.sysUserAgentModal.title = '用户代理人设置'
      },
      passwordModalOk() {
        //TODO 密码修改完成 不需要刷新页面，可以把datasource中的数据更新一下
      }
    },
    created() {
      this.warehouseList()
    }

  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>