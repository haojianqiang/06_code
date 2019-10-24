// NIM会话
import store from '@/store'
// onSessions只在初始化完成后回调
export function onSessions(sessions){
    console.log(sessions);
    // 
    console.log("onSessions")
    // store.commit('updateSessions',sessions);
}

// 会话更新
export function onUpdateSession(session){
    console.log("会话更新了")
    console.log(session);
    // let sessions = [session];
    // store.commit('updateSessions',sessions);
}