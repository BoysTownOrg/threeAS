clear
debouncer;
readscript
% keyboard
close
plot(nextkey/44100)
hold on
plot(AS3out132356.RT/1000-.100,'r')
ylim([0.1 0.9])
legend('RT from photosensor/microphone','RT from program - 100 msec');
annotation('textarrow', [0.29 0.25 ], [0.85 0.85],'String','Mic didn''t trigger');
annotation('textarrow', [0.29 0.18 ], [0.845 0.845],'String','');
