window.callback = function(res){
console.log(res)
// res（用户主动关闭验证码）= {ret: 2, ticket: null}
// res（验证成功） = {ret: 0, ticket: "String", randstr: "String"}
if(res.ret === 0){
this.console.log("ticket:"+res.ticket);
this.console.log("randstr:"+res.randstr);
GraphicVerificationView.graphicVerificationResults(res.ret,res.ticket,res.randstr);
}else{
GraphicVerificationView.graphicVerificationResults(res.ret,"","");
}
}