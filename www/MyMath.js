// cordova.define("SimpleMath.contactimportandexport", function(require, exports, module) {
// cordova.define("SimpleMath.MyMath", function(require, exports, module) {
    // var exec = require('cordova/exec');
    // var myMathFunc = function(){};

    // myMathFunc.prototype.plus = function(arg0, success, error) {
    //     exec(success, error, "MyMath", "Plus", arg0);
    // };

    // myMathFunc.prototype.minus = function(arg0, success, error) {
    //     exec(success, error, "MyMath", "Minus", arg0);
    // };

    // var MYMATHFUNC = new myMathFunc();
    // module.exports = MYMATHFUNC;

    // exports.MyMath = function(arg0, success, error) {
    //     exec(success, error, "MyMath", "Minus", [arg0]);
    // };



    var exec = require('cordova/exec');

    // var MyMath = {
    //     show:function() {
    //         exec(null, null, "MyMath", "plus", []);
    //     }

    // };

    var myAPI = {}

    myAPI.plus = function(arg0,arg1, success, error) {
        alert("aaaa");
        exec(success, error, "MyMath", "plus", [arg0,arg1]);
    };
    // 打开文件夹
    myAPI.folder = function(success,error){
        exec(success, error, "MyMath", "folder", []);
    }

    //打开文件
    myAPI.file = function(success,error){
        exec(success,error,"MyMath","file",[]);

    }

    //导入文件

    myAPI.import = function(success,error){
         exec(success,error,"MyMath","import",[]);
    }

    //导出文件
    myAPI.export = function(success,error){
          exec(success,error,"MyMath","export",[]);
    }



      myAPI.openself = function(success,error){
              exec(success,error,"MyMath","fileself",[]);
        }


    //访问android自带文件系统

//    myAPI.openself = (success,error){
//          exec(success,error,"MyMath","fileself",[]);
//    }
    //缓存大小
    myAPI.getcachesize = function(success,error){
        exec(success,error,"MyMath","getcachesize",[]);
    }


    //缓存清楚

    myAPI.clearcache = function(success,error){
        exec(success,error,"MyMath","clearcache",[]);

    }

    myAPI.updateapk = function(success,error){
        exec(success,error,"MyMath","updateapk",[]);
    }


    module.exports = myAPI;







    // });

// });
