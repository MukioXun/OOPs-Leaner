# 编译
javac -d build/classes -sourcepath src/main/java src/main/java/shootemup/Main.java src/main/java/shootemup/core/*.java src/main/java/shootemup/entities/*.java src/main/java/shootemup/audio/*.java

# 运行
java -cp build/classes shootemup.Main