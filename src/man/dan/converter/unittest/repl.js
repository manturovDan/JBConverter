function PermutationsWithRepetition(src, len){

    var K = len - 1, k = 0,
        N = src.length, n = 0,
        out = [],
        stack = [];

    function next(){
        while (true) {
            while (n < src.length) {
                out[k] = src[n++];
                if (k == K) return out.slice(0);
                else {
                    if (n < src.length) {
                        stack.push(k);
                        stack.push(n);
                    }
                    k++;
                    n = 0;
                }
            }
            if (stack.length == 0) break;

            n = stack.pop();
            k = stack.pop();
        }
        return false;
    }

    function rewind(){ k = 0; n = 0; out = []; stack = []; }

    function each(cb) {
        rewind();
        var v;
        while (v = next()) if (cb(v) === false) return;
    }

    return {
        next: next,
        each: each,
        rewind: rewind
    };
}

var Nsize = set.length;
//if (Nsize < Msize)
//	throw {"source": "Msize", "message": resources["m"] };

var tset = [];
for(var i in set)
    tset.push(set[i].value);

var perms = PermutationsWithRepetition(tset, Msize);

while(perms.next()) ;

var i = 1;
perms.each(function(v){
    var rec = arrangements.AddNewRecord();
    rec.num = i++;
    rec.arrangement = v;
});