<template>
  <a-modal
    v-model="visible"
    :width="width">
    <!-- 自定义内容-begin -->
    <a-form
      :form="form"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }">
      <a-form-item label="导出反馈说明">
        <a-input v-model="explain" placeholder="请输入反馈说明"></a-input>
      </a-form-item>
    </a-form>
    <!-- 自定义内容-END -->
    <!-- 自定义页脚-begin -->
    <template slot="footer">
      <a-button type="primary" @click="handleExportXls('计划表1')">导出反馈excel</a-button>
      <a-button @click="handleCancel">关闭</a-button>
    </template>
    <!-- 自定义页脚-END -->
  </a-modal>
</template>

<script>
  import { downFile } from '../../../api/manage'

  export default {
    name: 'Plan1ListExportModal',
    data() {
      return {
        description: '计划表1导出excel弹出框组件',
        form: this.$form.createForm(this),
        width: 800,
        visible: false,
        // 导出所需反馈说明
        explain: '',
        url: {
          exportXlsUrl: '/cable/plan1/exportXls'
        }
      }
    },
    methods: {
      // 导出反馈excel
      handleExportXls(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        downFile(this.url.exportXlsUrl, { explain: this.explain }).then((data) => {
          if (!data) {
            this.$message.warning('文件下载失败')
            this.visible = false // 关闭显示框
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
            this.visible = false // 关闭显示框
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
            this.visible = false // 关闭显示框
          }
        })
      },
      // 显示弹出框
      showModal() {
        this.visible = true
      },
      // 关闭弹出框
      handleCancel() {
        this.visible = false
      }
    }
  }
</script>

<style scoped>

</style>