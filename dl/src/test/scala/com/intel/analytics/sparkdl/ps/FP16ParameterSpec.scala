/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.analytics.sparkdl.ps

import com.intel.analytics.sparkdl.tensor.TensorNumericMath.TensorNumeric.TensorNumericDouble
import com.intel.analytics.sparkdl.utils.Engine
import org.scalatest.{FlatSpec, Matchers}
import com.intel.analytics.sparkdl.tensor.Tensor

class FP16ParameterSpec extends FlatSpec with Matchers {

  "convert double tensor to fp16 array and back" should "be same when the number is integer" in {
    val tensor = Tensor[Double](5)
    tensor.setValue(1, 1.0)
    tensor.setValue(2, 2.0)
    tensor.setValue(3, 3.0)
    tensor.setValue(4, 4.0)
    tensor.setValue(5, 5.0)

    val target = tensor.clone().zero()

    val parameter = new FP16Parameter[Double](tensor)
    parameter.copyTo(target)

    target should be(tensor)
  }

  it should "get a truncated value when the number is float with to many mantissa" in {
    val tensor = Tensor[Double](5)
    tensor.setValue(1, 1.111111)
    tensor.setValue(2, 2.111111)
    tensor.setValue(3, 3.111111)
    tensor.setValue(4, 4.111111)
    tensor.setValue(5, 5.111111)

    val parameter = new FP16Parameter[Double](tensor)
    parameter.copyTo(tensor)

    val target = Tensor[Double](5)
    target.setValue(1, 1.109375)
    target.setValue(2, 2.109375)
    target.setValue(3, 3.109375)
    target.setValue(4, 4.09375)
    target.setValue(5, 5.09375)

    tensor should be(target)
  }

  it should "be correct when only perform on a slice" in {
    val tensor = Tensor[Double](5)
    tensor.setValue(1, 1.111111)
    tensor.setValue(2, 2.111111)
    tensor.setValue(3, 3.111111)
    tensor.setValue(4, 4.111111)
    tensor.setValue(5, 5.111111)

    val parameter = new FP16Parameter[Double](tensor)
    val buffer = parameter.bytes(1, 2)
    val parameter2 = new FP16Parameter[Double](buffer)
    val test = tensor.clone()
    parameter2.copyTo(0, test, 1, 2)

    val target = Tensor[Double](5)
    target.setValue(1, 1.111111)
    target.setValue(2, 2.109375)
    target.setValue(3, 3.109375)
    target.setValue(4, 4.111111)
    target.setValue(5, 5.111111)

    test should be(target)
  }

  it should "throw exception when slice size is not consisted" in {
    val tensor = Tensor[Double](5)
    val parameter = new FP16Parameter[Double](tensor)
    val buffer = parameter.bytes(1, 2)
    val parameter2 = new FP16Parameter[Double](buffer)
    intercept[IllegalArgumentException] {
      parameter2.copyTo(0, tensor, 1, 3)
    }
  }

  it should "be same when the number is integer in single thread env" in {
    val tensor = Tensor[Double](5)
    tensor.setValue(1, 1.0)
    tensor.setValue(2, 2.0)
    tensor.setValue(3, 3.0)
    tensor.setValue(4, 4.0)
    tensor.setValue(5, 5.0)

    Engine.setCoreNum(1)
    val param = new FP16Parameter(tensor)

    val test = tensor.clone().zero
    param.copyTo(test)

    test should be(tensor)
  }

  it should "be same when the number is integer in too many thread env" in {
    val tensor = Tensor[Double](5)
    tensor.setValue(1, 1.0)
    tensor.setValue(2, 2.0)
    tensor.setValue(3, 3.0)
    tensor.setValue(4, 4.0)
    tensor.setValue(5, 5.0)

    Engine.setCoreNum(500)
    val param = new FP16Parameter(tensor)

    val test = tensor.clone().zero
    param.copyTo(test)

    test should be(tensor)
  }

  "convert float tensor to fp16 array and back" should "be same when the number is integer" in {
    val tensor = Tensor[Float](5)
    tensor.setValue(1, 1.0f)
    tensor.setValue(2, 2.0f)
    tensor.setValue(3, 3.0f)
    tensor.setValue(4, 4.0f)
    tensor.setValue(5, 5.0f)

    val target = tensor.clone().zero()

    val parameter = new FP16Parameter[Float](tensor)
    parameter.copyTo(target)

    target should be(tensor)
  }

  it should "get a truncated value when the number is float with to many mantissa" in {
    val tensor = Tensor[Float](5)
    tensor.setValue(1, 1.111111f)
    tensor.setValue(2, 2.111111f)
    tensor.setValue(3, 3.111111f)
    tensor.setValue(4, 4.111111f)
    tensor.setValue(5, 5.111111f)

    val parameter = new FP16Parameter[Float](tensor)
    parameter.copyTo(tensor)

    val target = Tensor[Float](5)
    target.setValue(1, 1.109375f)
    target.setValue(2, 2.109375f)
    target.setValue(3, 3.109375f)
    target.setValue(4, 4.09375f)
    target.setValue(5, 5.09375f)

    tensor should be(target)
  }

  it should "be correct when only perform on a slice" in {
    val tensor = Tensor[Float](5)
    tensor.setValue(1, 1.111111f)
    tensor.setValue(2, 2.111111f)
    tensor.setValue(3, 3.111111f)
    tensor.setValue(4, 4.111111f)
    tensor.setValue(5, 5.111111f)

    val parameter = new FP16Parameter[Float](tensor)
    val buffer = parameter.bytes(1, 2)
    val parameter2 = new FP16Parameter[Float](buffer)
    val test = tensor.clone()
    parameter2.copyTo(0, test, 1, 2)

    val target = Tensor[Float](5)
    target.setValue(1, 1.111111f)
    target.setValue(2, 2.109375f)
    target.setValue(3, 3.109375f)
    target.setValue(4, 4.111111f)
    target.setValue(5, 5.111111f)

    test should be(target)
  }

  it should "throw exception when slice size is not consisted" in {
    val tensor = Tensor[Float](5)
    val parameter = new FP16Parameter[Float](tensor)
    val buffer = parameter.bytes(1, 2)
    val parameter2 = new FP16Parameter[Float](buffer)
    intercept[IllegalArgumentException] {
      parameter2.copyTo(0, tensor, 1, 3)
    }
  }

  it should "be same when the number is integer in single thread env" in {
    val tensor = Tensor[Float](5)
    tensor.setValue(1, 1.0f)
    tensor.setValue(2, 2.0f)
    tensor.setValue(3, 3.0f)
    tensor.setValue(4, 4.0f)
    tensor.setValue(5, 5.0f)

    Engine.setCoreNum(1)
    val param = new FP16Parameter(tensor)

    val test = tensor.clone().zero
    param.copyTo(test)

    test should be(tensor)
  }

  it should "be same when the number is integer in too many thread env" in {
    val tensor = Tensor[Float](5)
    tensor.setValue(1, 1.0f)
    tensor.setValue(2, 2.0f)
    tensor.setValue(3, 3.0f)
    tensor.setValue(4, 4.0f)
    tensor.setValue(5, 5.0f)

    Engine.setCoreNum(500)
    val param = new FP16Parameter(tensor)

    val test = tensor.clone().zero
    param.copyTo(test)

    test should be(tensor)
  }

  "Add bytes to double parameter" should "be correct" in {
    val tensor1 = Tensor[Double](5)
    tensor1.setValue(1, 1.0)
    tensor1.setValue(2, 2.0)
    tensor1.setValue(3, 3.0)
    tensor1.setValue(4, 4.0)
    tensor1.setValue(5, 5.0)
    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Double](5)
    tensor2.setValue(1, 2.0)
    tensor2.setValue(2, 3.0)
    tensor2.setValue(3, 4.0)
    tensor2.setValue(4, 5.0)
    tensor2.setValue(5, 6.0)
    val param2 = new FP16Parameter(tensor2)

    val test = tensor1 + tensor2
    param1.parAdd(param2.bytes())
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  it should "be correct in serialize version" in {
    val tensor1 = Tensor[Double](5)
    tensor1.setValue(1, 1.0)
    tensor1.setValue(2, 2.0)
    tensor1.setValue(3, 3.0)
    tensor1.setValue(4, 4.0)
    tensor1.setValue(5, 5.0)

    val param1 = new FP16Parameter(tensor1)
    val tensor2 = Tensor[Double](5)
    tensor2.setValue(1, 2.0)
    tensor2.setValue(2, 3.0)
    tensor2.setValue(3, 4.0)
    tensor2.setValue(4, 5.0)
    tensor2.setValue(5, 6.0)

    val param2 = new FP16Parameter(tensor2)
    val test = tensor1 + tensor2
    param1.add(param2.bytes())
    param1.copyTo(tensor1)
    tensor1 should be(test)
  }

  it should "be correct with slice" in {
    val tensor1 = Tensor[Double](5)
    tensor1.setValue(1, 1.0)
    tensor1.setValue(2, 2.0)
    tensor1.setValue(3, 3.0)
    tensor1.setValue(4, 4.0)
    tensor1.setValue(5, 5.0)

    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Double](5)
    tensor2.setValue(1, 2.0)
    tensor2.setValue(2, 3.0)
    tensor2.setValue(3, 4.0)
    tensor2.setValue(4, 5.0)
    tensor2.setValue(5, 6.0)

    val param2 = new FP16Parameter(tensor2)
    val test = Tensor[Double](5)
    test.setValue(1, 1.0)
    test.setValue(2, 5.0)
    test.setValue(3, 7.0)
    test.setValue(4, 4.0)
    test.setValue(5, 5.0)

    param1.parAdd(param2.bytes(1, 2), 1, 2)
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  "Add bytes to float parameter" should "be correct" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)
    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Float](5)
    tensor2.setValue(1, 2.0f)
    tensor2.setValue(2, 3.0f)
    tensor2.setValue(3, 4.0f)
    tensor2.setValue(4, 5.0f)
    tensor2.setValue(5, 6.0f)
    val param2 = new FP16Parameter(tensor2)

    val test = tensor1 + tensor2
    param1.parAdd(param2.bytes())
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  it should "be correct in serialize version" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)

    val param1 = new FP16Parameter(tensor1)
    val tensor2 = Tensor[Float](5)
    tensor2.setValue(1, 2.0f)
    tensor2.setValue(2, 3.0f)
    tensor2.setValue(3, 4.0f)
    tensor2.setValue(4, 5.0f)
    tensor2.setValue(5, 6.0f)

    val param2 = new FP16Parameter(tensor2)
    val test = tensor1 + tensor2
    param1.add(param2.bytes())
    param1.copyTo(tensor1)
    tensor1 should be(test)
  }

  it should "be correct with slice" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)

    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Float](5)
    tensor2.setValue(1, 2.0f)
    tensor2.setValue(2, 3.0f)
    tensor2.setValue(3, 4.0f)
    tensor2.setValue(4, 5.0f)
    tensor2.setValue(5, 6.0f)

    val param2 = new FP16Parameter(tensor2)
    val test = Tensor[Float](5)
    test.setValue(1, 1.0f)
    test.setValue(2, 5.0f)
    test.setValue(3, 7.0f)
    test.setValue(4, 4.0f)
    test.setValue(5, 5.0f)

    param1.parAdd(param2.bytes(1, 2), 1, 2)
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  it should "be correct with slice in single thread" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)

    Engine.setCoreNum(1)
    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Float](5)
    tensor2.setValue(1, 2.0f)
    tensor2.setValue(2, 3.0f)
    tensor2.setValue(3, 4.0f)
    tensor2.setValue(4, 5.0f)
    tensor2.setValue(5, 6.0f)

    val param2 = new FP16Parameter(tensor2)
    val test = Tensor[Float](5)
    test.setValue(1, 1.0f)
    test.setValue(2, 5.0f)
    test.setValue(3, 7.0f)
    test.setValue(4, 4.0f)
    test.setValue(5, 5.0f)

    param1.parAdd(param2.bytes(1, 2), 1, 2)
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  it should "be correct with slice in too many threads" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)

    Engine.setCoreNum(100)
    val param1 = new FP16Parameter(tensor1)

    val tensor2 = Tensor[Float](5)
    tensor2.setValue(1, 2.0f)
    tensor2.setValue(2, 3.0f)
    tensor2.setValue(3, 4.0f)
    tensor2.setValue(4, 5.0f)
    tensor2.setValue(5, 6.0f)

    val param2 = new FP16Parameter(tensor2)
    val test = Tensor[Float](5)
    test.setValue(1, 1.0f)
    test.setValue(2, 5.0f)
    test.setValue(3, 7.0f)
    test.setValue(4, 4.0f)
    test.setValue(5, 5.0f)

    param1.parAdd(param2.bytes(1, 2), 1, 2)
    param1.copyTo(tensor1)

    tensor1 should be(test)
  }

  "copy from double tensor" should "be correct" in {
    val tensor1 = Tensor[Double](5)
    tensor1.setValue(1, 1.0)
    tensor1.setValue(2, 2.0)
    tensor1.setValue(3, 3.0)
    tensor1.setValue(4, 4.0)
    tensor1.setValue(5, 5.0)

    val params = new FP16Parameter(tensor1)
    val tensor2 = tensor1 * 2

    params.copyFrom(tensor2)
    params.copyTo(tensor1)

    tensor1 should be(tensor2)
  }

  "copy from float tensor" should "be correct" in {
    val tensor1 = Tensor[Float](5)
    tensor1.setValue(1, 1.0f)
    tensor1.setValue(2, 2.0f)
    tensor1.setValue(3, 3.0f)
    tensor1.setValue(4, 4.0f)
    tensor1.setValue(5, 5.0f)

    val params = new FP16Parameter(tensor1)
    val tensor2 = tensor1 * 2

    params.copyFrom(tensor2)
    params.copyTo(tensor1)

    tensor1 should be(tensor2)
  }

}