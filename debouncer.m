newData1 = importdata('AS3out-13-23-56.wav');
ii=1;
incval=44100*.5; %skip ahead 500 msec
chan1=[];
while ii <length(newData1.data)
    if (newData1.data(ii,1) > .2)
        chan1 = [chan1,ii];
        ii = ii + incval;
    else
        ii = ii + 1;
    end
end
ii=1;
chan2=[];
while ii <length(newData1.data)
    if (newData1.data(ii,2) > .2)
        chan2 = [chan2,ii];
        ii = ii + incval;
    else
        ii = ii + 1;
    end
end
       
nextkey = [];
for jj = 1:length(chan2)-1
    tmp = chan1 - chan2(jj);
    mintmp = min(tmp(tmp>0));
    midx = find(mintmp==tmp);
    nextkey = [nextkey,tmp(midx)];
end
        
        
        