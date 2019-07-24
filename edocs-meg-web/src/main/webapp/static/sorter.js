const _search = ["ę", "ó", "ą", "ś", "ł", "ż", "ź", "ć", "ń"];
const _replace = ["ezz", "ozz", "azz", "szz", "lzz", "zz_", "zzz", "czz", "nzz"];

function sort_machines(array) {
    return array.sort((a, b) => {
        
        ad = a.idDescription.toLowerCase();
        bd = b.idDescription.toLowerCase();
        for (var i = 0; i < _search.length; i++) {
            ad = ad.replace(_search[i], _replace[i]);
            bd = bd.replace(_search[i], _replace[i]);
        }
        if (ad < bd) {
            return -1;
        }
        if (ad > bd) {
            return 1;
        }
        return 0;
    });
}

