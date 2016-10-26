data = dlmread('data.online.scores');
data_sum = sum(data);
(sum(data(:,2).*data(:,3))-1000*mean(data(:,2))*mean(data(:,3)))/999/std(data(:,2))/std(data(:,3))