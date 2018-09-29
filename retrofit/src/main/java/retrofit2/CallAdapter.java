/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

/**
 * Adapts a {@link Call} with response type {@code R} into the type of {@code T}. Instances are
 * created by {@linkplain Factory a factory} which is
 * {@linkplain Retrofit.Builder#addCallAdapterFactory(Factory) installed} into the {@link Retrofit}
 * instance.
 */
public interface CallAdapter<R, T> {
    /**
     * 返回将Http响应通过解析器解析后的Java对象类型
     */
    Type responseType();

    /**
     * 这里的R其实就是{@link #responseType}返回的类型，T就是对R类型进行了CallAdapter处理后的类型
     * 例如使用RxJavaCallAdapter时，假设响应的Entry是Person，那么R就是Person，没有经过RxJavaCallAdapter处理的
     * 接口返回就是Response<Person>,经过RxJavaCallAdapter处理的接口返回就是Observable<Person>
     * 而这个Observable<Person>就是T
     */
    T adapt(Call<R> call);

    /**
     * Creates {@link CallAdapter} instances based on the return type of {@linkplain
     * Retrofit#create(Class) the service interface} methods.
     */
    abstract class Factory {
        /**
         * Returns a call adapter for interface methods that return {@code returnType}, or null if it
         * cannot be handled by this factory.
         */
        public abstract @Nullable
        CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit);

        /**
         * Extract the upper bound of the generic parameter at {@code index} from {@code type}. For
         * example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
         */
        protected static Type getParameterUpperBound(int index, ParameterizedType type) {
            return Utils.getParameterUpperBound(index, type);
        }

        /**
         * Extract the raw class type from {@code type}. For example, the type representing
         * {@code List<? extends Runnable>} returns {@code List.class}.
         */
        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
