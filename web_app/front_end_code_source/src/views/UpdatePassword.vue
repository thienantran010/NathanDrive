<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="500px"
      :showCancel="true"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
        @submit.prevent
      >
        <!--input输入-->
        <el-form-item label="new password" prop="password">
          <el-input
            type="password"
            size="large"
            placeholder="please write your password"
            v-model="formData.password"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--input输入-->
        <el-form-item label="check password" prop="rePassword">
          <el-input
            type="password"
            size="large"
            placeholder="please write your password again"
            v-model="formData.rePassword"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import AvatarUpload from "@/components/AvatarUpload.vue";

import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  updatePassword: "updatePassword",
};

const formData = ref({});
const formDataRef = ref();

const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.rePassword) {
    callback(new Error(rule.message));
  } else {
    callback();
  }
};

const rules = {
  password: [
    { required: true, message: "please write your password" },
    {
      validator: proxy.Verify.password,
      message: "password can only consist of numbers, letters, special characters. 8-18 digits",
    },
  ],
  rePassword: [
    { required: true, message: "please write your password again" },
    {
      validator: checkRePassword,
      message: "two passwords are not the same",
    },
  ],
};
const show = () => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = {};
  });
};

defineExpose({ show });

const dialogConfig = ref({
  show: false,
  title: "change password",
  buttons: [
    {
      type: "primary",
      text: "yes",
      click: (e) => {
        submitForm();
      },
    },
  ],
});

const submitForm = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let result = await proxy.Request({
      url: api.updatePassword,
      params: {
        password: formData.value.password,
      },
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("reset password success");
  });
};
</script>

<style lang="scss">
</style>