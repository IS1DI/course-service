package com.is1di.courses.service.grpc;

import com.is1di.courses.entity.courses.Course;
import com.is1di.courses.service.CourseService;
import com.university.userservice.grpc.course.CourseRequest;
import com.university.userservice.grpc.course.CourseResponse;
import com.university.userservice.grpc.course.CourseServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
public class CourseGrpcServer extends CourseServiceGrpc.CourseServiceImplBase {
    private final CourseService courseService;

    @Override
    public void createCourse(CourseRequest request, StreamObserver<CourseResponse> responseObserver) {
        String id = courseService.create(new Course(request.getTitle(),request.getDirectionTitle(),request.getTeacherId(),request.getTeacherFullName(),request.getDescription(),request.getStartAt(),request.getEndsAt())).getTitle();
        responseObserver.onNext(CourseResponse.newBuilder().setCourseId(id).build());
        responseObserver.onCompleted();
    }
}
