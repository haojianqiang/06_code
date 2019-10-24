let Utils = Object.create(null);

// 格式化时间
Utils.formatDate = function(datetime, simple = false) {
  let tempDate = new Date().getTime();
  let result = this.stringifyDate(datetime, simple);
  let thatDay = result.thatDay;
  let deltaTime = (tempDate - thatDay) / 1000;

  if (deltaTime < 3600 * 24) {
    return result.withHour;
  } else if (deltaTime < 3600 * 24 * 2) {
    return result.withLastDay;
  } else if (deltaTime < 3600 * 24 * 7) {
    return result.withDay;
  } else if (deltaTime < 3600 * 24 * 30) {
    return result.withMonth;
  } else {
    return result.withYear;
  }
};

Utils.stringifyDate = function(datetime, simple = false) {
  // let weekMap = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
  let weekMap = [
    "星期日",
    "星期一",
    "星期二",
    "星期三",
    "星期四",
    "星期五",
    "星期六"
  ];
  datetime = new Date(datetime);
  let year = datetime.getFullYear();
  let simpleYear = datetime.getYear() - 100;
  let month = datetime.getMonth() + 1;
  month = month > 9 ? month : "0" + month;
  let day = datetime.getDate();
  day = day > 9 ? day : "0" + day;
  let hour = datetime.getHours();
  hour = hour > 9 ? hour : "0" + hour;
  let min = datetime.getMinutes();
  min = min > 9 ? min : "0" + min;
  let week = datetime.getDay();
  week = weekMap[week];
  let thatDay = new Date(year, month - 1, day, 0, 0, 0).getTime();

  if (simple) {
    return {
      withYear: `${day}/${month}/${simpleYear}`,
      withMonth: `${month}-${day}`,
      withDay: `${week}`,
      withLastDay: `昨天`,
      withHour: `${hour}:${min}`,
      thatDay
    };
  } else {
    return {
      withYear: `${year}-${month}-${day} ${hour}:${min}`,
      withMonth: `${month}-${day} ${hour}:${min}`,
      withDay: `${week} ${hour}:${min}`,
      withLastDay: `昨天 ${hour}:${min}`,
      withHour: `${hour}:${min}`,
      thatDay
    };
  }
};

// 格式化日期
// 示例: utils.formatTime(new Date(1542274800000),"yyyy-MM-dd")
Utils.formatTime = function(datetime, format) {
  var o = {
    "M+": datetime.getMonth() + 1, //month
    "d+": datetime.getDate(), //day
    "h+": datetime.getHours(), //hour
    "m+": datetime.getMinutes(), //minute
    "s+": datetime.getSeconds(), //second
    "q+": Math.floor((datetime.getMonth() + 3) / 3), //quarter
    S: datetime.getMilliseconds() //millisecond
  };
  if (/(y+)/.test(format)) {
    format = format.replace(
      RegExp.$1,
      (datetime.getFullYear() + "").substr(4 - RegExp.$1.length)
    );
  }
  for (var k in o) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(
        RegExp.$1,
        RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)
      );
    }
  }
  return format;
};

Utils.msgType = {
  MSG_TEXT: 0,
  MSG_IMAGE: 1,
  MSG_AUDIO: 2,
  MSG_VIDEO: 3
};

Utils.whoType = {
  SENDER: 0,
  RECEIVER: 1
};

export default Utils;
