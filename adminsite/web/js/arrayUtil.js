Array.prototype.inArray = function (value) {
  var i;
  for (i=0; i<this.length; i++) {
    if (this[i] === value) {
      return i;
    }
  }
  return -1;
};

function compSort(a,b) {
  if (a < b) { return -1; }
  else if (a > b) { return 1; }
  else { return 0; }
}

Array.prototype.sortNum = function() {
   return this.sort(compSort);
}

