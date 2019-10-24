/**
 *  格式化表格字段工具
 */

// 将以;分隔的字符串切分并添加换行符
export function formatterLineFeed(value) {
    let result = "";
    let arr = value.split(';');
    if (arr.length < 2) {
        return value;
    } else {
        for (let i = 0; i < arr.length; i++) {
            result += arr[i];
            if (i != arr.length - 1) {
                result += "<hr style='margin: 2px 0 !important;'>";
            }
        }
        return result;
    }
}
// 是否围标
export function formatterCleanBidder(value) {
    let result = "";
    let arr = value.split(';');
    if (arr.length < 2) {
        return value;
    } else {
        for (let i = 0; i < arr.length; i++) {
            result += (arr[i] == "0" ? '&nbsp;' : '可能围标');
            if (i != arr.length - 1) {
                result += "<hr style='margin: 2px 0 !important;'>";
            }
        }
        return result;
    }
}
// 标记出围标的股东、法人、高管 realName：围标人的名称
export function formatterYellow(value, realName) {
    let result = "";
    let arr = value.split(';');
    if (arr.length < 2) {
        return value;
    } else {
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].indexOf(realName) != -1) {
                arr[i] = "<font style='background-color:#ffe4c4'>" + arr[i] + "</font>";
            }
            result += arr[i];
            if (i != arr.length - 1) {
                result += "<hr style='margin: 2px 0 !important;color:black !important'>";
            }
        }
        return result;
    }
}
// 格式化金额
export function formatterAmount(value) {
    value = value * 10000;
    return value.toFixed(2);
}

export function formatterData(fmt, date) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份   
        "d+": date.getDate(),                    //日   
        "h+": date.getHours(),                   //小时   
        "m+": date.getMinutes(),                 //分   
        "s+": date.getSeconds(),                 //秒   
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度   
        "S": date.getMilliseconds()             //毫秒   
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


