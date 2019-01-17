M=csvread('D:\FELIPE\RESULTADOS\BASE5\Data261218_Paretao.csv')
figure(1);
scatter(M(:,1),M(:,2),'+');
%axis([40,50,30400,31000])
xlabel('Makespan');
ylabel('Custo');

figure(2);
hist(M(:,1));
figure(3);
hist(M(:,2));

