import{u as se,b as ae}from"./vue-router-128bc7ad.js";import{_ as oe}from"./index-29e2e257.js";import{j as te,r,ag as c,aq as ne,o as t,c as d,a as s,Q as le,F as P,V as _,U as v,O as m,P as I,u as z,T as M,S as y,bh as ie,bf as re}from"./@vue-7298a6f3.js";import"./element-plus-857d32b3.js";import"./lodash-es-36eb724a.js";import"./@vueuse-740eda15.js";import"./@element-plus-925c7e21.js";import"./@popperjs-c75af06c.js";import"./@ctrl-f8748455.js";import"./dayjs-c95324ad.js";import"./aplayer-d80f7f58.js";import"./async-validator-10c6301d.js";import"./memoize-one-297ddbcb.js";import"./escape-html-85da7166.js";import"./normalize-wheel-es-ed76fb12.js";import"./@floating-ui-d10d3e5d.js";import"./vue-cookies-9b55b892.js";import"./@highlightjs-b388fc70.js";import"./highlight.js-cc51af62.js";import"./axios-4a70c6fc.js";import"./docx-preview-b41ea745.js";import"./jszip-d9c085e9.js";import"./xlsx-04f2268f.js";import"./vue-pdf-embed-acd0b5a4.js";import"./vue-9ac0364e.js";import"./dplayer-ff9c2365.js";import"./vue-clipboard3-abb1cd37.js";import"./clipboard-2fdbe17c.js";const w=S=>(ie("data-v-063b0de7"),S=S(),re(),S),ce={class:"share"},de=w(()=>s("span",{class:"iconfont icon-pan"},null,-1)),ue=w(()=>s("span",{class:"name"},"Nathan Drive",-1)),pe=[de,ue],he={class:"share-body"},fe={key:0,class:"loading"},_e={class:"share-panel"},ve={class:"share-user-info"},me={class:"avatar"},ye={class:"share-info"},Se={class:"user-info"},ge={class:"nick-name"},ke={class:"share-time"},Ie={class:"file-name"},we={class:"share-op-btn"},be=w(()=>s("span",{class:"iconfont icon-cancel"},null,-1)),Ce=w(()=>s("span",{class:"iconfont icon-import"},null,-1)),Fe={class:"file-list"},Ne=["onMouseenter","onMouseleave"],Te=["title"],Re=["onClick"],De={class:"op"},xe=["onClick"],Pe=["onClick"],ze={key:0},Me={__name:"Share",setup(S){const{proxy:n}=te(),p=se(),b=ae(),u={getShareLoginInfo:"/showShare/getShareLoginInfo",loadFileList:"/showShare/loadFileList",createDownloadUrl:"/showShare/createDownloadUrl",download:"/api/showShare/download",cancelShare:"/share/cancelShare",saveShare:"/showShare/saveShare"},i=b.params.shareId,l=r({});(async()=>{let e=await n.Request({url:u.getShareLoginInfo,showLoading:!1,params:{shareId:i}});if(e){if(e.data==null){p.push("/shareCheck/"+i);return}l.value=e.data}})();const O=[{label:"file name",prop:"fileName",scopedSlots:"fileName"},{label:"modify time",prop:"lastUpdateTime",width:200},{label:"size",prop:"fileSize",scopedSlots:"fileSize",width:200}],h=r({}),U={extHeight:80,selectType:"checkbox"},C=async()=>{let e={pageNo:h.value.pageNo,pageSize:h.value.pageSize,shareId:i,filePid:T.value.fileId},o=await n.Request({url:u.loadFileList,params:e});o&&(h.value=o.data)},L=e=>{h.value.list.forEach(o=>{o.showOp=!1}),e.showOp=!0},V=e=>{e.showOp=!1},f=r([]),q=e=>{f.value=[],e.forEach(o=>{f.value.push(o.fileId)})},T=r({fileId:0}),j=e=>{const{curFolder:o}=e;T.value=o,C()},R=r(),D=r(),$=e=>{if(e.folderType==1){D.value.openFolder(e);return}e.shareId=i,R.value.showPreview(e,2)},B=async e=>{let o=await n.Request({url:u.createDownloadUrl+"/"+i+"/"+e});o&&(window.location.href=u.download+"/"+o.data)},g=r(),F=[],A=()=>{if(f.value.length!=0){if(!n.VueCookies.get("userInfo")){p.push("/login?redirectUrl="+b.path);return}F.values=f.value,g.value.showFolderDialog()}},E=e=>{if(!n.VueCookies.get("userInfo")){p.push("/login?redirectUrl="+b.path);return}F.values=[e.fileId],g.value.showFolderDialog()},H=async e=>{await n.Request({url:u.saveShare,params:{shareId:i,shareFileIds:F.values.join(","),myFolderId:e}})&&(C(),n.Message.success("save success"),g.value.close())},Q=()=>{n.Confirm("do you really want to cancel share？",async()=>{await n.Request({url:u.cancelShare,params:{shareIds:i}})&&(n.Message.success("cancel share success"),p.push("/"))})},G=()=>{p.push("/")};return(e,o)=>{const J=c("Avatar"),x=c("el-button"),K=c("Navigation"),N=c("icon"),W=c("Table"),X=c("FolderSelect"),Y=c("Preview"),Z=ne("loading");return t(),d("div",ce,[s("div",{class:"header"},[s("div",{class:"header-content"},[s("div",{class:"logo",onClick:G},pe)])]),s("div",he,[Object.keys(l.value).length==0?le((t(),d("div",fe,null,512)),[[Z,Object.keys(l.value).length==0]]):(t(),d(P,{key:1},[s("div",_e,[s("div",ve,[s("div",me,[_(J,{userId:l.value.userId,avatar:l.value.avatar,width:50},null,8,["userId","avatar"])]),s("div",ye,[s("div",Se,[s("span",ge,v(l.value.nickName),1),s("span",ke,"share at "+v(l.value.shareTime),1)]),s("div",Ie,"share file："+v(l.value.fileName),1)])]),s("div",we,[l.value.currentUser?(t(),m(x,{key:0,type:"primary",onClick:Q},{default:I(()=>[be,M("cancel share")]),_:1})):(t(),m(x,{key:1,type:"primary",disabled:f.value.length==0,onClick:A},{default:I(()=>[Ce,M("save to my drive")]),_:1},8,["disabled"]))])]),_(K,{ref_key:"navigationRef",ref:D,onNavChange:j,shareId:z(i)},null,8,["shareId"]),s("div",Fe,[_(W,{columns:O,showPagination:!0,dataSource:h.value,fetch:C,initFetch:!1,options:U,showPageSize:!1,onRowSelected:q},{fileName:I(({index:ee,row:a})=>[s("div",{class:"file-item",onMouseenter:k=>L(a),onMouseleave:k=>V(a)},[(a.fileType==3||a.fileType==1)&&a.status!==0?(t(),m(N,{key:0,cover:a.fileCover},null,8,["cover"])):(t(),d(P,{key:1},[a.folderType==0?(t(),m(N,{key:0,fileType:a.fileType},null,8,["fileType"])):y("",!0),a.folderType==1?(t(),m(N,{key:1,fileType:0})):y("",!0)],64)),s("span",{class:"file-name",title:a.fileName},[s("span",{onClick:k=>$(a)},v(a.fileName),9,Re)],8,Te),s("span",De,[a.folderType==0?(t(),d("span",{key:0,class:"iconfont icon-download",onClick:k=>B(a.fileId)},"download",8,xe)):y("",!0),a.showOp&&!l.value.currentUser?(t(),d("span",{key:1,class:"iconfont icon-import",onClick:k=>E(a)},"save to my drive",8,Pe)):y("",!0)])],40,Ne)]),fileSize:I(({index:ee,row:a})=>[a.fileSize?(t(),d("span",ze,v(z(n).Utils.size2Str(a.fileSize)),1)):y("",!0)]),_:1},8,["dataSource"])])],64)),_(X,{ref_key:"folderSelectRef",ref:g,onFolderSelect:H},null,512),_(Y,{ref_key:"previewRef",ref:R},null,512)])])}}},ds=oe(Me,[["__scopeId","data-v-063b0de7"]]);export{ds as default};