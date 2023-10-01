<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="600px"
      :showCancel="showCancel"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="100px"
        @submit.prevent
      >
        <el-form-item label="file"> {{ formData.fileName }} </el-form-item>
        <template v-if="showType == 0">
          <el-form-item label="valid date" prop="validType">
            <el-radio-group v-model="formData.validType">
              <el-radio :label="0">1天</el-radio>
              <el-radio :label="1">7天</el-radio>
              <el-radio :label="2">30天</el-radio>
              <el-radio :label="3">永久有效</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="access code" prop="codeType">
            <el-radio-group v-model="formData.codeType">
              <el-radio :label="0">自定义</el-radio>
              <el-radio :label="1">系统生成</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="code" v-if="formData.codeType == 0">
            <el-input
              clearable
              placeholder="please write 5-digit access code"
              v-model.trim="formData.code"
              maxLength="5"
              :style="{ width: '130px' }"
            ></el-input>
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item label="share link">
            {{ shareUrl }}{{ resultInfo.shareId }}
          </el-form-item>
          <el-form-item label="access code">
            {{ resultInfo.code }}
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="copy">copy link and access code</el-button>
          </el-form-item>
        </template>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import useClipboard from "vue-clipboard3";
const { toClipboard } = useClipboard();
import { ref, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();

const shareUrl = ref(document.location.origin + "/share/");

const api = {
  shareFile: "/share/shareFile",
};
const showType = ref(0);
const formData = ref({});
const formDataRef = ref();
const rules = {
  validType: [{ required: true, message: "choose valid date" }],
  codeType: [{ required: true, message: "choose access code" }],
  code: [
    { required: true, message: "please write access code" },
    { validator: proxy.Verify.shareCode, message: "access code must be numbers" },
    { min: 5, message: "access code must be 5 digits long" },
  ],
};

const showCancel = ref(true);
const dialogConfig = ref({
  show: false,
  title: "share",
  buttons: [
    {
      type: "primary",
      text: "yes",
      click: (e) => {
        share();
      },
    },
  ],
});

const resultInfo = ref({});
const share = async () => {
  if (Object.keys(resultInfo.value).length > 0) {
    dialogConfig.value.show = false;
    return;
  }
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: api.shareFile,
      params: params,
    });
    if (!result) {
      return;
    }
    showType.value = 1;
    resultInfo.value = result.data;
    dialogConfig.value.buttons[0].text = "close";
    showCancel.value = false;
  });
};

const show = (data) => {
  showCancel.value = true;
  dialogConfig.value.show = true;
  showType.value = 0;
  resultInfo.value = {};
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = Object.assign({}, data);
  });
};

defineExpose({ show });

const copy = async () => {
  await toClipboard(
    `link:${shareUrl.value}${resultInfo.value.shareId} access code: ${resultInfo.value.code}`
  );
  proxy.Message.success("copy success");
};
</script>

<style lang="scss" scoped>
</style>
