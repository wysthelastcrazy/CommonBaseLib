package com.wys.commonbaselib.jetpack;

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:jetpack简述
 */
class Note {
    /**
     * Lifecycle
     *
     * 生命周期感知型组件可执行操作来相应另一个组件（如Activity和Fragment）的生命周期状态的变化。
     * 这些组件有助于写出更有调理且更精简的代码，这样的代码更易于维护。
     *
     * 生命周期：
     * Lifecycle是一个类，用于存储有关组件（如Activity或Fragment）的生命周期状态
     * 信息，并允许其他对象观察此状态。
     * Lifecycle使用两种主要枚举跟踪其关联组件的生命周期状态：Event和State
     *
     * LifecycleOwner
     *
     * LifecycleOwner是单一方法接口，表示类具有Lifecycle。它具有一种方法（getLifecycle()），
     * 该方法必须有类实现。如果尝试管理整个应该进程的生命周期，请参阅ProcessLifecycleOwner
     * 此接口从各个类（如Fragment和AppCompatActivity）抽象化Lifecycle的所有权，
     * 并允许编写与这些类搭配使用的组件。
     * 任何自定义应用类都可以实现LifecycleOwner接口
     */


    /**
     * LiveData
     *
     * LiveData是一种可观察的数据存储器类。
     * 与常规的可观察类不同，LiveData具有生命周期感知能力，
     * 它遵循其他应用组件（如Activity、Fragment或Service）的生命周期。
     * 这种感知能力可以确保LiveData仅更新处于活跃生命周期状态的应用组件观察者。
     *
     * 如果观察者（Observer类表示）的生命周期处于STARTED或RESUMED状态，
     * 则LiveData会认为该观察者处于活跃状态。LiveData只会将更新通知给活跃的
     * 观察者，为观察LiveData对象而注册的非活跃观察者不回收到通知。
     *
     * 可以注册与实现LifecycleOwner接口的对象配对的观察者。有了这种对应关系，
     * 当相应的Lifecycle对象的状态变为DESTROYED时，便可移除此观察者。这对于
     * Activity和Fragment特别有用，因为它们可以放心的观察LiveData对象而不必担心泄露，
     * 当Activity和Fragment的生命周期被销毁时，系统会立即退订它们。
     *
     * 使用LiveData的优势
     *
     *  - 确保界面符合数据状态
     *    LiveData遵循观察者模式，当生命周期状态发生变化时，LiveData会通知Observer对象。
     *    可以整合代码以便在这些Observer对象中更新界面。观察者可以每次发生变更时更新界面，
     *    而不是在每次应用数据发生变更时更新界面。
     *
     *  - 不回发生内存泄露
     *    观察者会绑定到Lifecycle对象，并在其关联的生命周期遭到销毁后进行自我清理。
     *
     *  - 不回因为Activity停止而导致奔溃
     *    如果观察者的生命周期处于非活跃状态（如返回栈中的Activity），则它不会接收任何LiveData事件
     *
     *  - 不再需要手动处理生命周期
     *    界面组件只是观察相关数据，不会停止或恢复观察。LiveData将自动管理所有这些操作，
     *    因为它在观察时可以感知相关的生命周期状态变化。
     *
     *  - 数据始终保持最新状态
     *    如果生命周期变为非活跃状态，它会再次变为活跃状态时接收最新的数据。例如，曾经在后台
     *    的Activity会在返回前台后立即接收最新的数据。
     *
     *  - 适当的配置更改
     *    如果由于配置更改（如设备旋转）而重新创建了Activity或Fragment，它会立即接收最新的可用数据
     *
     *  - 共享资源
     *    可以使用单一实例模式扩展LiveData对象以封装系统服务，以便在应用中共享它们。
     *    LiveData对象连接到系统服务一次，然后需要相应资源的任何观察者只需要观察LiveData对象。
     *    详情请参阅扩展Liveata
     */
}
