import{j as O,r as w,k as Q,_ as A,ag as _,o as p,c as C,a as s,V as r,P as o,n as L,O as b,S as m,T as x,a9 as G,a3 as H,U as J,bh as W,bf as X}from"./@vue-7298a6f3.js";import{u as Y,b as Z}from"./vue-router-128bc7ad.js";import{m as ee}from"./js-md5-cda431b7.js";import{_ as oe}from"./index-29e2e257.js";import"./aplayer-d80f7f58.js";import"./element-plus-857d32b3.js";import"./lodash-es-36eb724a.js";import"./@vueuse-740eda15.js";import"./@element-plus-925c7e21.js";import"./@popperjs-c75af06c.js";import"./@ctrl-f8748455.js";import"./dayjs-c95324ad.js";import"./async-validator-10c6301d.js";import"./memoize-one-297ddbcb.js";import"./escape-html-85da7166.js";import"./normalize-wheel-es-ed76fb12.js";import"./@floating-ui-d10d3e5d.js";import"./vue-cookies-9b55b892.js";import"./@highlightjs-b388fc70.js";import"./highlight.js-cc51af62.js";import"./axios-4a70c6fc.js";import"./docx-preview-b41ea745.js";import"./jszip-d9c085e9.js";import"./xlsx-04f2268f.js";import"./vue-pdf-embed-acd0b5a4.js";import"./vue-9ac0364e.js";import"./dplayer-ff9c2365.js";import"./vue-clipboard3-abb1cd37.js";import"./clipboard-2fdbe17c.js";const se="/assets/qq-8fddb348.png";const c=q=>(W("data-v-213e5a34"),q=q(),X(),q),ae={class:"login-body"},le=c(()=>s("div",{class:"bg"},null,-1)),te={class:"login-panel"},re=c(()=>s("div",{class:"login-title"},"Nathan Drive",-1)),ie=c(()=>s("span",{class:"iconfont icon-account"},null,-1)),ne=c(()=>s("span",{class:"iconfont icon-password"},null,-1)),de={key:1},ue={class:"send-emali-panel"},ce=c(()=>s("span",{class:"iconfont icon-checkcode"},null,-1)),pe=c(()=>s("div",null,[s("p",null,"1、Check auth code in trash bin"),s("p",null,"2、Under avatar of your email->setting->no trash->white sheet->set email to white sheet"),s("p",null," 3、Don't know how to set【1500422315@qq.com】into white sheet？ ")],-1)),me=c(()=>s("span",{class:"a-link",style:{"font-size":"14px"}},"didn't receive email auth code？",-1)),fe=c(()=>s("span",{class:"iconfont icon-account"},null,-1)),ve=c(()=>s("span",{class:"iconfont icon-password"},null,-1)),ge=c(()=>s("span",{class:"iconfont icon-password"},null,-1)),we={class:"check-code-panel"},_e=c(()=>s("span",{class:"iconfont icon-checkcode"},null,-1)),he=["src"],ke={class:"rememberme-panel"},Ce={class:"no-account"},ye={key:0},Ve={key:1},be={key:2},qe={key:5,class:"login-btn-qq"},Me={class:"check-code-panel"},xe=c(()=>s("span",{class:"iconfont icon-checkcode"},null,-1)),Pe=["src"],Re={__name:"Login",setup(q){const{proxy:u}=O(),N=Y(),R=Z(),f={checkCode:"/api/checkCode",sendMailCode:"/sendEmailCode",register:"/register",login:"/login",resetPwd:"/resetPwd",qqlogin:"/qqlogin"},l=w(1),v=i=>{l.value=i,T()};Q(()=>{v(1)});const j=(i,e,n)=>{e!==t.value.registerPassword?n(new Error(i.message)):n()},t=w({}),M=w(),U={email:[{required:!0,message:"please write email"},{validator:u.Verify.email,message:"please write a valid email"}],password:[{required:!0,message:"please write password"}],emailCode:[{required:!0,message:"please write email auth code"}],nickName:[{required:!0,message:"please write nickName"}],registerPassword:[{required:!0,message:"please write password"},{validator:u.Verify.password,message:"Password only consists of numbers, letters, special characters. 8-18 digits"}],reRegisterPassword:[{required:!0,message:"please write your password again"},{validator:j,message:"two passwords are not the same!"}],checkCode:[{required:!0,message:"please write image auth code"}]},I=w(),D=w(),h=i=>{i==0?I.value=f.checkCode+"?type="+i+"&time="+new Date().getTime():D.value=f.checkCode+"?type="+i+"&time="+new Date().getTime()},y=w({}),P=w(),k=A({show:!1,title:"Send email auth code",buttons:[{type:"primary",text:"send auth code",click:()=>{E()}}]}),$=()=>{M.value.validateField("email",i=>{i&&(k.show=!0,L(()=>{h(1),P.value.resetFields(),y.value={email:t.value.email}}))})},E=()=>{P.value.validate(async i=>{if(!i)return;const e=Object.assign({},y.value);e.type=l.value==0?0:1,await u.Request({url:f.sendMailCode,params:e,errorCallback:()=>{h(1)}})&&(u.Message.success("email auth code send successfully, please check in your email"),k.show=!1)})},T=()=>{L(()=>{if(h(0),M.value.resetFields(),t.value={},l.value==1){const i=u.VueCookies.get("loginInfo");i&&(t.value=i)}})},S=()=>{M.value.validate(async i=>{if(!i)return;let e={};if(Object.assign(e,t.value),(l.value==0||l.value==2)&&(e.password=e.registerPassword,delete e.registerPassword,delete e.reRegisterPassword),l.value==1){let g=u.VueCookies.get("loginInfo"),V=g==null?null:g.password;e.password!==V&&(e.password=ee(e.password))}let n=null;l.value==0?n=f.register:l.value==1?n=f.login:l.value==2&&(n=f.resetPwd);let d=await u.Request({url:n,params:e,errorCallback:()=>{h(0)}});if(d)if(l.value==0)u.Message.success("register successfully, please log in"),v(1);else if(l.value==1){if(e.rememberMe){const V={email:e.email,password:e.password,rememberMe:e.rememberMe};u.VueCookies.set("loginInfo",V,"7d")}else u.VueCookies.remove("loginInfo");u.Message.success("log in successfully"),u.VueCookies.set("userInfo",d.data,0);const g=R.query.redirectUrl||"/";N.push(g)}else l.value==2&&(u.Message.success("reset password successfully, please log in"),v(1))})},B=async()=>{let i=await u.Request({url:f.qqlogin,params:{callbackUrl:R.query.redirectUrl||""}});i&&(u.VueCookies.remove("userInfo"),document.location.href=i.data)};return(i,e)=>{const n=_("el-input"),d=_("el-form-item"),g=_("el-button"),V=_("el-popover"),F=_("el-checkbox"),z=_("el-form"),K=_("Dialog");return p(),C("div",ae,[le,s("div",te,[r(z,{class:"login-register",model:t.value,rules:U,ref_key:"formDataRef",ref:M},{default:o(()=>[re,r(d,{prop:"email"},{default:o(()=>[r(n,{size:"large",clearable:"",placeholder:"please write your email",modelValue:t.value.email,"onUpdate:modelValue":e[0]||(e[0]=a=>t.value.email=a),modelModifiers:{trim:!0},maxLength:"150"},{prefix:o(()=>[ie]),_:1},8,["modelValue"])]),_:1}),l.value==1?(p(),b(d,{key:0,prop:"password"},{default:o(()=>[r(n,{type:"password",size:"large",placeholder:"please write your password",modelValue:t.value.password,"onUpdate:modelValue":e[1]||(e[1]=a=>t.value.password=a),modelModifiers:{trim:!0},"show-password":""},{prefix:o(()=>[ne]),_:1},8,["modelValue"])]),_:1})):m("",!0),l.value==0||l.value==2?(p(),C("div",de,[r(d,{prop:"emailCode"},{default:o(()=>[s("div",ue,[r(n,{size:"large",placeholder:"please write email auth code",modelValue:t.value.emailCode,"onUpdate:modelValue":e[2]||(e[2]=a=>t.value.emailCode=a),modelModifiers:{trim:!0}},{prefix:o(()=>[ce]),_:1},8,["modelValue"]),r(g,{class:"send-mail-btn",type:"primary",size:"large",onClick:$},{default:o(()=>[x("get auth code")]),_:1})]),r(V,{placement:"left",width:500,trigger:"click"},{reference:o(()=>[me]),default:o(()=>[pe]),_:1})]),_:1}),l.value==0?(p(),b(d,{key:0,prop:"nickName"},{default:o(()=>[r(n,{size:"large",clearable:"",placeholder:"please write your nickName",modelValue:t.value.nickName,"onUpdate:modelValue":e[3]||(e[3]=a=>t.value.nickName=a),modelModifiers:{trim:!0},maxLength:"20"},{prefix:o(()=>[fe]),_:1},8,["modelValue"])]),_:1})):m("",!0),r(d,{prop:"registerPassword"},{default:o(()=>[r(n,{type:"password",size:"large",placeholder:"please write your password",modelValue:t.value.registerPassword,"onUpdate:modelValue":e[4]||(e[4]=a=>t.value.registerPassword=a),modelModifiers:{trim:!0},"show-password":""},{prefix:o(()=>[ve]),_:1},8,["modelValue"])]),_:1}),r(d,{prop:"reRegisterPassword"},{default:o(()=>[r(n,{type:"password",size:"large",placeholder:"please write your password again",modelValue:t.value.reRegisterPassword,"onUpdate:modelValue":e[5]||(e[5]=a=>t.value.reRegisterPassword=a),modelModifiers:{trim:!0},"show-password":""},{prefix:o(()=>[ge]),_:1},8,["modelValue"])]),_:1})])):m("",!0),r(d,{prop:"checkCode"},{default:o(()=>[s("div",we,[r(n,{size:"large",placeholder:"please write auth code",modelValue:t.value.checkCode,"onUpdate:modelValue":e[6]||(e[6]=a=>t.value.checkCode=a),modelModifiers:{trim:!0},onKeyup:G(S,["enter"])},{prefix:o(()=>[_e]),_:1},8,["modelValue","onKeyup"]),s("img",{src:I.value,class:"check-code",onClick:e[7]||(e[7]=a=>h(0))},null,8,he)])]),_:1}),l.value==1?(p(),b(d,{key:2},{default:o(()=>[s("div",ke,[r(F,{modelValue:t.value.rememberMe,"onUpdate:modelValue":e[8]||(e[8]=a=>t.value.rememberMe=a)},{default:o(()=>[x("Remeber me")]),_:1},8,["modelValue"])]),s("div",Ce,[s("a",{href:"javascript:void(0)",class:"a-link",onClick:e[9]||(e[9]=a=>v(2))},"forget password? "),s("a",{href:"javascript:void(0)",class:"a-link",onClick:e[10]||(e[10]=a=>v(0))},"no account? ")])]),_:1})):m("",!0),l.value==0?(p(),b(d,{key:3},{default:o(()=>[s("a",{href:"javascript:void(0)",class:"a-link",onClick:e[11]||(e[11]=a=>v(1))},"already has account?")]),_:1})):m("",!0),l.value==2?(p(),b(d,{key:4},{default:o(()=>[s("a",{href:"javascript:void(0)",class:"a-link",onClick:e[12]||(e[12]=a=>v(1))},"Log in?")]),_:1})):m("",!0),r(d,null,{default:o(()=>[r(g,{type:"primary",class:"op-btn",onClick:S,size:"large"},{default:o(()=>[l.value==0?(p(),C("span",ye,"Register")):m("",!0),l.value==1?(p(),C("span",Ve,"Log in")):m("",!0),l.value==2?(p(),C("span",be,"Reset password")):m("",!0)]),_:1})]),_:1}),l.value==1?(p(),C("div",qe,[x(" Quick Login(must be qq email) "),s("img",{src:se,onClick:B})])):m("",!0)]),_:1},8,["model"])]),r(K,{show:k.show,title:k.title,buttons:k.buttons,width:"500px",showCancel:!1,onClose:e[16]||(e[16]=a=>k.show=!1)},{default:o(()=>[r(z,{model:y.value,rules:U,ref_key:"formData4SendMailCodeRef",ref:P,"label-width":"80px",onSubmit:e[15]||(e[15]=H(()=>{},["prevent"]))},{default:o(()=>[r(d,{label:"Email"},{default:o(()=>[x(J(t.value.email),1)]),_:1}),r(d,{label:"auth code",prop:"checkCode"},{default:o(()=>[s("div",Me,[r(n,{size:"large",placeholder:"please write auth code again",modelValue:y.value.checkCode,"onUpdate:modelValue":e[13]||(e[13]=a=>y.value.checkCode=a),modelModifiers:{trim:!0}},{prefix:o(()=>[xe]),_:1},8,["modelValue"]),s("img",{src:D.value,class:"check-code",onClick:e[14]||(e[14]=a=>h(1))},null,8,Pe)])]),_:1})]),_:1},8,["model"])]),_:1},8,["show","title","buttons"])])}}},to=oe(Re,[["__scopeId","data-v-213e5a34"]]);export{to as default};
