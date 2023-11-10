param ($Ver = $("latest"))


function build
{
    ($Tag = ("is1di/courseservice:$Ver"))
    ./gradlew clean build
    docker build -t $Tag .
    docker push $Tag
}
build